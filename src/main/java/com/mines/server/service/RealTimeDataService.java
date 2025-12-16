package com.mines.server.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mines.server.config.AesConfig;
import com.mines.server.model.request.AppRequest;
import com.mines.server.model.request.RealTimeData;
import com.mines.server.model.response.AppResponse;
import com.mines.server.model.data.*;
import com.mines.server.repository.*;
import com.mines.server.util.AesUtils;
import com.mines.server.util.ValidationUtils;
import java.util.List;

/**
 * 实时数据处理核心服务（解密、验证、解析、存储）
 */
@Slf4j
@Service
public class RealTimeDataService {

    @Value("${aes.iv}")
    private String iv;

    @Autowired
    private AesConfig aesConfig;

    @Autowired
    private PowerDataRepository powerDataRepository;

    @Autowired
    private HostLoadDataRepository hostLoadDataRepository;

    @Autowired
    private HostLoadAlarmDataRepository hostLoadAlarmDataRepository;

    @Autowired
    private PointRealDataRepository pointRealDataRepository;

    @Autowired
    private PointStatDataRepository pointStatDataRepository;

    @Autowired
    private PointAbnormalDataRepository pointAbnormalDataRepository;

    @Autowired
    private PersonLocationDataRepository personLocationDataRepository;

    @Autowired
    private OvertimeAlarmDataRepository overtimeAlarmDataRepository;

    @Autowired
    private OvercrowdAlarmDataRepository overcrowdAlarmDataRepository;

    @Autowired
    private RestrictedAreaDataRepository restrictedAreaDataRepository;

    @Autowired
    private PersonHelpDataRepository personHelpDataRepository;

    @Autowired
    private SubstationRunDataRepository substationRunDataRepository;

    @Autowired
    private AlertDataRepository alertDataRepository;

    /**
     * 处理实时数据上报请求
     */
    public AppResponse processData(AppRequest request) throws Exception {
        String dataId = request.getDataId();
        try {
            log.info("processData request : {} ", request);

            // 1. 验证外层请求字段
            ValidationUtils.validateAppRequest(request);

            log.info("processData appid : {} ", request.getAppId());

            // 2. 验证serviceId并获取密钥
            String serviceId = request.getServiceId();
            log.info("processData serviceId : {} ", serviceId);
            if (!aesConfig.getServiceKeyMap().containsKey(serviceId)) {
                return AppResponse.fail("401", "INVALID_SERVICE_ID", "未授权的serviceId", dataId);
            }
            String secretKey = aesConfig.getServiceKeyMap().get(serviceId);
            log.info("processData secretKey : {} ,iv: {}", secretKey,iv);
            // 3. AES解密data字段
            String decryptedData = AesUtils.decrypt(request.getData(), secretKey,iv);

            log.info("processData decryptedData : {} ", decryptedData);

            // 4. 解析并验证实时数据主体
            RealTimeData realTimeData = JSON.parseObject(decryptedData, RealTimeData.class);
            ValidationUtils.validateRealTimeData(realTimeData);

            log.info("processData realTimeData : {} ", realTimeData);

            // 5. 根据dataType解析并存储数据
            String dataType = realTimeData.getDataType();
            List<?> datas = realTimeData.getDatas();
            log.info("processData dataType : {} ", dataType);
            log.info("processData datas : {} ", datas);

            switch (dataType) {
                case "01":
                    List<PowerData> powerDatas = JSON.parseArray(JSON.toJSONString(datas), PowerData.class);
                    ValidationUtils.validatePowerData(powerDatas);
                    powerDataRepository.saveAll(powerDatas);
                    break;
                case "02":
                    List<HostLoadData> hostLoadDatas = JSON.parseArray(JSON.toJSONString(datas), HostLoadData.class);
                    ValidationUtils.validateHostLoadData(hostLoadDatas);
                    hostLoadDataRepository.saveAll(hostLoadDatas);
                    break;
                case "03":
                    List<HostLoadAlarmData> alarmDatas = JSON.parseArray(JSON.toJSONString(datas), HostLoadAlarmData.class);
                    ValidationUtils.validateHostLoadAlarmData(alarmDatas);
                    hostLoadAlarmDataRepository.saveAll(alarmDatas);
                    break;
                case "04":
                    List<PointRealData> pointRealDatas = JSON.parseArray(JSON.toJSONString(datas), PointRealData.class);
                    ValidationUtils.validatePointRealData(pointRealDatas);
                    pointRealDataRepository.saveAll(pointRealDatas);
                    break;
                case "05":
                    List<PointStatData> pointStatDatas = JSON.parseArray(JSON.toJSONString(datas), PointStatData.class);
                    ValidationUtils.validatePointStatData(pointStatDatas);
                    pointStatDataRepository.saveAll(pointStatDatas);
                    break;
                case "06":
                    List<PointAbnormalData> abnormalDatas = JSON.parseArray(JSON.toJSONString(datas), PointAbnormalData.class);
                    ValidationUtils.validatePointAbnormalData(abnormalDatas);
                    pointAbnormalDataRepository.saveAll(abnormalDatas);
                    break;
                case "07":
                    List<PersonLocationData> locationDatas = JSON.parseArray(JSON.toJSONString(datas), PersonLocationData.class);
                    ValidationUtils.validatePersonLocationData(locationDatas);
                    personLocationDataRepository.saveAll(locationDatas);
                    break;
                case "08":
                    List<OvertimeAlarmData> overtimeDatas = JSON.parseArray(JSON.toJSONString(datas), OvertimeAlarmData.class);
                    ValidationUtils.validateOvertimeAlarmData(overtimeDatas);
                    overtimeAlarmDataRepository.saveAll(overtimeDatas);
                    break;
                case "09":
                    List<OvercrowdAlarmData> overcrowdDatas = JSON.parseArray(JSON.toJSONString(datas), OvercrowdAlarmData.class);
                    ValidationUtils.validateOvercrowdAlarmData(overcrowdDatas);
                    overcrowdAlarmDataRepository.saveAll(overcrowdDatas);
                    break;
                case "10":
                    List<RestrictedAreaData> restrictedDatas = JSON.parseArray(JSON.toJSONString(datas), RestrictedAreaData.class);
                    ValidationUtils.validateRestrictedAreaData(restrictedDatas);
                    restrictedAreaDataRepository.saveAll(restrictedDatas);
                    break;
                case "11":
                    List<PersonHelpData> helpDatas = JSON.parseArray(JSON.toJSONString(datas), PersonHelpData.class);
                    ValidationUtils.validatePersonHelpData(helpDatas);
                    personHelpDataRepository.saveAll(helpDatas);
                    break;
                case "12":
                    List<SubstationRunData> substationDatas = JSON.parseArray(JSON.toJSONString(datas), SubstationRunData.class);
                    ValidationUtils.validateSubstationRunData(substationDatas);
                    substationRunDataRepository.saveAll(substationDatas);
                    break;
                case "1001":
                    List<AlertData> alertDatas = JSON.parseArray(JSON.toJSONString(datas), AlertData.class);
                    if (alertDatas != null && !alertDatas.isEmpty()) {
                        // 简单校验必填项
                        for (AlertData data : alertDatas) {
                            if (data.getKsbh() == null || data.getKsbh().isEmpty()) {
                                throw new IllegalArgumentException("AlertData.ksbh 不能为空");
                            }
                            if (data.getAlarmStartTime() == null) {
                                throw new IllegalArgumentException("AlertData.alarmStartTime 不能为空");
                            }
                        }
                        // 保存到数据库
                        alertDataRepository.saveAll(alertDatas);
                        log.info("成功保存 {} 条1001报警数据", alertDatas.size());
                    }
                    break;
                default:
                    return AppResponse.fail("400", "INVALID_DATA_TYPE", "不支持的数据类型：" + dataType, dataId);
            }

            return AppResponse.success(dataId);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail("400", "BUSINESS_ERROR", e.getMessage(), dataId);
        }
    }
}
