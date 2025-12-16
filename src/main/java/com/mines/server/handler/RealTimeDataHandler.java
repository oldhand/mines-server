package com.mines.server.handler;

import com.alibaba.fastjson.JSON;
import com.mines.server.config.AesConfig;
import com.mines.server.model.data.RiskObjectDTO;
import com.mines.server.model.data.StaticData;
import com.mines.server.model.request.RealTimeData;
import com.mines.server.model.request.RiskObjectRequest;
import com.mines.server.model.request.TcpAppRequest;
import com.mines.server.model.response.AppResponse;
import com.mines.server.model.vo.PersonStatVO;
import com.mines.server.model.vo.PowerStatVO;
import com.mines.server.model.vo.SystemStatusVO;
import com.mines.server.repository.AlertDataRepository;
import com.mines.server.service.*;
import com.mines.server.util.AesUtils;
import com.mines.server.util.ValidationUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 扩展的实时数据处理器，支持多接口功能
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class RealTimeDataHandler extends SimpleChannelInboundHandler<String> {

    @Value("${aes.iv}")
    private String iv;

    @Autowired
    private AesConfig aesConfig;

    @Autowired
    private RealTimeDataService realTimeDataService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private RiskObjectService riskObjectService;

    @Autowired
    private StaticDataService staticDataService;

    @Autowired
    private AlertDataRepository alertDataRepository;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        AppResponse response;
        String clientIp = ctx.channel().remoteAddress().toString();
        long startTime = System.currentTimeMillis();

        try {
            // 1. 解析TCP请求（包含接口标识）
            TcpAppRequest request = JSON.parseObject(msg, TcpAppRequest.class);
            ValidationUtils.validateAppRequest(request); // 复用现有验证工具

            String dataId = request.getDataId();
            log.info("processData appid : {} ", request.getAppId());

            // 2. 验证serviceId并获取密钥
            String serviceId = request.getServiceId();
            log.info("processData serviceId : {} ", serviceId);
            if (!aesConfig.getServiceKeyMap().containsKey(serviceId)) {
                response = AppResponse.fail("401", "INVALID_SERVICE_ID", "未授权的serviceId", dataId);
                String responseMsg = JSON.toJSONString(response) + "@@";
                ctx.writeAndFlush(responseMsg);
                return;
            }
            String secretKey = aesConfig.getServiceKeyMap().get(serviceId);
            log.info("processData secretKey : {} ,iv: {}", secretKey, iv);

            // 3. AES解密data字段
            String decryptedData = AesUtils.decrypt(request.getData(), secretKey, iv);
            log.info("processData decryptedData : {} ", decryptedData);

            // 4. 解析并验证实时数据主体
            // 注意：此处验证确保了 decryptedData 是符合 RealTimeData 结构的 JSON
            RealTimeData realTimeData = JSON.parseObject(decryptedData, RealTimeData.class);
            ValidationUtils.validateRealTimeData(realTimeData);

            log.info("processData realTimeData : {} ", realTimeData);
            String dataType = realTimeData.getDataType();
            List<?> datas = realTimeData.getDatas();
            log.info("processData dataType : {} ", dataType);
            log.info("processData datas : {} ", datas);

            // === 判空处理 ===
            // 如果接口标识为空，默认为实时数据上报（兼容旧版本或测试代码）
            String interfaceCode = request.getInterfaceCode();
            if (interfaceCode == null || interfaceCode.trim().isEmpty()) {
                interfaceCode = "REALTIME_REPORT";
            }

            // 根据接口标识路由到不同处理逻辑
            switch (interfaceCode) {
                // === 实时数据上报 ===
                case "REALTIME_REPORT":
                    // 实时数据上报（对应RealTimeDataController）
                    // ProcessData 方法内部会再次解密，所以传入原始 request
                    response = realTimeDataService.processData(request);
                    break;

                // === 风险双重预防机制相关数据（与DataReceiveController一致） ===
                case "RECEIVE_OBJECT_LIST":
                    // 处理风险对象数据（对应RiskObjectService）
                    // 修正：从已解密的 realTimeData 中提取数据，而非解析加密的 request.getData()
                    RiskObjectRequest riskRequest = new RiskObjectRequest();
                    String jsonDatas = JSON.toJSONString(realTimeData.getDatas());
                    List<RiskObjectDTO> objectList = JSON.parseArray(jsonDatas, RiskObjectDTO.class);
                    riskRequest.setObjectList(objectList);
                    response = riskObjectService.process(riskRequest, request.getDataId());
                    break;

                case "RECEIVE_IMGT_LIST":
                    // 接收风险空间分布图数据
                    response = realTimeDataService.processData(request);
                    break;

                case "RECEIVE_UNIT_LIST":
                    // 接收风险单元数据
                    response = realTimeDataService.processData(request);
                    break;

                case "RECEIVE_EVENT_LIST":
                    // 接收风险事件数据
                    response = realTimeDataService.processData(request);
                    break;

                case "RECEIVE_CONTROL_MEASURES_LIST":
                    // 接收管控措施数据
                    response = realTimeDataService.processData(request);
                    break;

                case "RECEIVE_MEASURES_TASK_LIST":
                    // 接收隐患排查任务数据
                    response = realTimeDataService.processData(request);
                    break;

                case "RECEIVE_MEASURES_TASK_RECORD_LIST":
                    // 接收隐患排查记录
                    response = realTimeDataService.processData(request);
                    break;

                case "RECEIVE_DANGER_INVESTIGATION_LIST":
                    // 接收隐患信息数据
                    response = realTimeDataService.processData(request);
                    break;

                // === 系统管理接口 ===
                case "SYSTEM_VERSION":
                    // 系统版本查询（对应SystemController）
                    response = AppResponse.success(request.getDataId());
                    response.setData(systemService.getSystemStatus().getServerVersion());
                    break;

                case "SYSTEM_STATUS":
                    // 系统状态查询（对应SystemController）
                    SystemStatusVO status = systemService.getSystemStatus();
                    response = AppResponse.success(request.getDataId());
                    response.setData(status);
                    break;

                // === 统计分析接口 ===
                case "STATISTICS_POWER":
                    // 用电量统计（对应StatisticsController）
                    PowerStatVO powerStat = statisticsService.statPower(
                            request.getParam("enterpriseCode"),
                            new Date(request.getParam("startTime")),
                            new Date(request.getParam("endTime")),
                            request.getParam("statType")
                    );
                    response = AppResponse.success(request.getDataId());
                    response.setData(powerStat);
                    break;

                case "STATISTICS_PERSON":
                    // 人员位置统计（对应StatisticsController）
                    PersonStatVO personStat = statisticsService.statPersonLocation(
                            request.getParam("enterpriseCode")
                    );
                    response = AppResponse.success(request.getDataId());
                    response.setData(personStat);
                    break;

                // === 静态数据填报接口 ===
                case "STATIC_DATA_SUBMIT":
                    // 静态数据提交（对应StaticDataController）
                    StaticData staticDataSubmit = JSON.parseObject(JSON.toJSONString(realTimeData.getDatas().get(0)), StaticData.class);
                    staticDataService.saveStaticData(staticDataSubmit);
                    response = AppResponse.success(request.getDataId());
                    response.setData("静态数据提交成功");
                    break;

                case "STATIC_DATA_UPDATE":
                    // 静态数据更新（对应StaticDataController）
                    StaticData staticDataUpdate = JSON.parseObject(JSON.toJSONString(realTimeData.getDatas().get(0)), StaticData.class);
                    staticDataService.updateStaticData(staticDataUpdate);
                    response = AppResponse.success(request.getDataId());
                    response.setData("静态数据更新成功");
                    break;

                case "STATIC_DATA_QUERY":
                    // 静态数据查询（对应StaticDataController）
                    StaticData queryData = staticDataService.getByEnterpriseCode(
                            request.getParam("enterpriseCode")
                    );
                    response = AppResponse.success(request.getDataId());
                    response.setData(queryData);
                    break;

                default:
                    response = AppResponse.fail(
                            "400",
                            "INVALID_INTERFACE",
                            "不支持的接口标识：" + request.getInterfaceCode(),
                            request.getDataId()
                    );
            }

            // 记录业务处理日志
            long costTime = System.currentTimeMillis() - startTime;
            log.info("[业务处理] 客户端:{} 接口:{} 处理成功，dataId:{}，耗时:{}ms",
                    clientIp, request.getInterfaceCode(), request.getDataId(), costTime);

        } catch (Exception e) {
            // 记录异常日志
            long costTime = System.currentTimeMillis() - startTime;
            log.error("[业务处理] 客户端:{} 处理失败，耗时:{}ms，错误:{}", clientIp, costTime, e.getMessage(), e);
            response = AppResponse.fail(
                    "400",
                    "SYSTEM_ERROR",
                    "请求处理失败：" + e.getMessage(),
                    null
            );
        }
        // 发送响应（添加@@分隔符）
        String responseMsg = JSON.toJSONString(response) + "@@";
        ctx.writeAndFlush(responseMsg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 连接建立时增加连接计数
        systemService.incrementConnectionCount();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 连接关闭时减少连接计数
        systemService.decrementConnectionCount();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("客户端连接异常：{}", cause.getMessage(), cause);
        ctx.close();
    }
}