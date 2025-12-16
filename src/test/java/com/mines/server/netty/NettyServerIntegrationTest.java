package com.mines.server.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.alibaba.fastjson.JSON;
import com.mines.server.MinesServerApplication;
import com.mines.server.model.request.AppRequest;
import com.mines.server.model.request.RealTimeData;
import com.mines.server.util.AesUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinesServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NettyServerIntegrationTest {

    // 从配置文件获取Netty服务端口
    @Value("${netty.tcp.port:8080}")
    private int nettyPort;

    // AES加密配置（需与服务端一致）
    private static final String AES_KEY = "1234567890ABCDEF";
    private static final String AES_IV = "0000000000000000";
    private static final String APP_ID = "3c9a6868a6d74e348708ad3f0c15c25b";
    private static final String SERVICE_ID = "c6314bc9888b4134bc9e6b989dd37679";

    private static EventLoopGroup group;

    @BeforeClass
    public static void init() {
        // 初始化客户端事件循环组
        group = new NioEventLoopGroup();
    }

    @AfterClass
    public static void destroy() {
        // 释放资源
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    /**
     * 测试场景：发送合法的实时数据报文，验证服务端处理结果
     */
    @Test
    public void testNettyServer_ValidData() throws Exception {
        // 1. 构建测试数据
        String dataId = "TEST_" + System.currentTimeMillis();
        RealTimeData realTimeData = buildTestRealTimeData(dataId);

        // 2. 加密数据
        String plainJson = JSON.toJSONString(realTimeData);
        String encryptedData = AesUtils.encrypt(plainJson, AES_KEY, AES_IV);

        // 3. 构建请求报文
        AppRequest request = new AppRequest();
        request.setAppId(APP_ID);
        request.setServiceId(SERVICE_ID);
        request.setDataId(dataId);
        request.setData(encryptedData);
        String requestMsg = JSON.toJSONString(request) + "@@"; // 添加分隔符

        // 4. 发送请求并等待响应
        CountDownLatch latch = new CountDownLatch(1);
        ResponseHandler handler = new ResponseHandler(latch, dataId);

        connectAndSend(requestMsg, handler);

        // 等待响应（超时时间5秒）
        boolean success = latch.await(5, TimeUnit.SECONDS);
        assertTrue("服务端未在规定时间内响应", success);
        assertTrue("响应状态应为成功", handler.isSuccess());
        assertEquals("响应dataId应与请求一致", dataId, handler.getResponseDataId());
    }

    @Test
    public void testNettyServer_ValidData_alert() throws Exception {
        // 1. 构建测试数据
        String dataId = "TEST_" + System.currentTimeMillis();
        RealTimeData realTimeData = buildTestRealTimeDataAlert(dataId);

        // 2. 加密数据
        String plainJson = JSON.toJSONString(realTimeData);
        String encryptedData = AesUtils.encrypt(plainJson, AES_KEY, AES_IV);

        // 3. 构建请求报文
        AppRequest request = new AppRequest();
        request.setAppId(APP_ID);
        request.setServiceId(SERVICE_ID);
        request.setDataId(dataId);
        request.setData(encryptedData);
        String requestMsg = JSON.toJSONString(request) + "@@"; // 添加分隔符

        // 4. 发送请求并等待响应
        CountDownLatch latch = new CountDownLatch(1);
        ResponseHandler handler = new ResponseHandler(latch, dataId);

        connectAndSend(requestMsg, handler);

        // 等待响应（超时时间5秒）
        boolean success = latch.await(5, TimeUnit.SECONDS);
        assertTrue("服务端未在规定时间内响应", success);
        assertTrue("响应状态应为成功", handler.isSuccess());
        assertEquals("响应dataId应与请求一致", dataId, handler.getResponseDataId());
    }


    /**
     * 连接Netty服务并发送数据
     */
    private void connectAndSend(String message, ResponseHandler handler) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 客户端编解码器配置（与服务端保持一致）
                        byte[] delimiterBytes = "@@".getBytes(StandardCharsets.UTF_8);
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(
                                10240,
                                Unpooled.wrappedBuffer(delimiterBytes)
                        ));
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(handler);
                    }
                });

        // 连接服务端并发送数据
        ChannelFuture future = bootstrap.connect("localhost", nettyPort).sync();
        future.channel().writeAndFlush(message).sync();
        future.channel().closeFuture().sync();
    }

    /**
     * 构建测试用的实时数据对象
     */
    private RealTimeData buildTestRealTimeData(String dataId) {
        RealTimeData data = new RealTimeData();
        data.setDataId(dataId);
        data.setEnterpriseCode("429021001003");
        data.setEnterpriseName("湖北神农磷业科技有限公司寨湾磷矿");
        data.setEnterpriseType("DXK");
        data.setGatewayCode("42902100100301");
        data.setCollectTime("2025-11-21 14:13:00");
        data.setIsConnectDataSource(true);
        data.setReportType("report");
        data.setDataType("04");

        // 添加测试数据项
        List<Map<String, Object>> datas = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("ksbh", "429021001003");
        item.put("pointCode", "42902100100301MN0010000001");
        item.put("sensorType", "0010");
        item.put("pointLocation", "840节点机房");
        item.put("pointValue", 20.41);
        item.put("valueUnit","%");
        item.put("pointStatus","00000000");
        item.put("dataTime", "2025-11-13 16:30:00");
        datas.add(item);
        item = new HashMap<>();
        item.put("ksbh", "429021001003");
        item.put("pointCode", "42902100100301MN0010000001");
        item.put("sensorType", "0010");
        item.put("pointLocation", "C8上山南770平巷");
        item.put("pointValue", 20.19);
        item.put("valueUnit","%");
        item.put("pointStatus","00000000");
        item.put("dataTime", "2025-11-13 16:30:00");
        datas.add(item);
        data.setDatas(datas);

        return data;
    }

    private RealTimeData buildTestRealTimeDataAlert(String dataId) {
        RealTimeData data = new RealTimeData();
        data.setDataId(dataId);
        data.setEnterpriseCode("429021001003");
        data.setEnterpriseName("湖北神农磷业科技有限公司寨湾磷矿");
        data.setEnterpriseType("DXK");
        data.setGatewayCode("42902100100301");
        data.setCollectTime("2025-11-21 14:13:00");
        data.setIsConnectDataSource(true);
        data.setReportType("report");
        data.setDataType("1001");

        // 添加测试数据项
        List<Map<String, Object>> datas = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("ksbh", "429021001003");
        item.put("alarmStartTime", "2025-11-20 11:32:15");
        item.put("alarmOverTime", "2025-11-20 13:22:45");
        item.put("alarmLevel", 1);
        item.put("alarmStatus", 0);
        item.put("alarmType", 1);
        item.put("relatedObj","417");
        item.put("Alarmcode","2953");
        item.put("alarmDesc", "蓝色告警：压力大于13.0MPa。\n");
        datas.add(item);
        data.setDatas(datas);

        return data;
    }
//    [35m2025-11-20 16:20:17[0;39m [34mINFO [0;39m [32mcom.mines.server.handler.RealTimeDataHandler[0;39m processData appid : 3c9a6868a6d74e348708ad3f0c15c25b
//[35m2025-11-20 16:20:17[0;39m [34mINFO [0;39m [32mcom.mines.server.handler.RealTimeDataHandler[0;39m processData serviceId : c6314bc9888b4134bc9e6b989dd37679
//[35m2025-11-20 16:20:17[0;39m [34mINFO [0;39m [32mcom.mines.server.handler.RealTimeDataHandler[0;39m processData secretKey : 1234567890ABCDEF ,iv: 0000000000000000
//            [35m2025-11-20 16:20:17[0;39m [34mINFO [0;39m [32mcom.mines.server.handler.RealTimeDataHandler[0;39m processData decryptedData :
//            {"dataId":"b94da045-413c-49d9-8d92-dc808c00ed5a",
//            "enterpriseCode":"429021001003",
//            "enterpriseType":"DXK",
//            "enterpriseName":"湖北神农磷业科技股份有限公司寨湾磷矿",
//            "gatewayCode":"42902100100301","collectTime":"2025-11-20 16:20:17",
//            "isConnectDataSource":true,
//            "reportType":"report",
//            "dataType":"1001","datas":[
//            {
//            "ksbh":"429021001003",
//            "alarmStartTime":"2025-11-20 11:32:15",
//            "alarmOverTime":"2025-11-20 13:22:45",
//            "alarmLevel":1,
//            "alarmStatus":0,
//            "alarmType":1,
//            "relatedObj":"417",
//            "Alarmcode":"2953",
//            "alarmDesc":"蓝色告警：压力大于13.0MPa。\n"}]}
//[35m2025-11-20 16:20:17[0;39m [34mINFO [0;39m [32mcom.mines.server.handler.RealTimeDataHandler[0;39m processData realTimeData : RealTimeData(dataId=b94da045-413c-49d9-8d92-dc808c00ed5a, enterpriseCode=429021001003, enterpriseType=DXK, enterpriseName=湖北神农磷业科技股份有限公司寨湾磷矿, gatewayCode=42902100100301, collectTime=2025-11-20 16:20:17, isConnectDataSource=true, reportType=report, dataType=1001, datas=[{"alarmDesc":"蓝色告警：压力大于13.0MPa。\n","alarmStatus":0,"alarmType":1,"relatedObj":"417","alarmOverTime":"2025-11-20 13:22:45","alarmStartTime":"2025-11-20 11:32:15","alarmLevel":1,"ksbh":"429021001003","Alarmcode":"2953"}])
//            [35m2025-11-20 16:20:17[0;39m [34mINFO [0;39m [32mcom.mines.server.handler.RealTimeDataHandler[0;39m processData dataType : 1001
//            [35m2025-11-20 16:20:17[0;39m [34mINFO [0;39m [32mcom.mines.server.handler.RealTimeDataHandler[0;39m processData datas : [{"alarmDesc":"蓝色告警：压力大于13.0MPa。\n","alarmStatus":0,"alarmType":1,"relatedObj":"417","alarmOverTime":"2025-11-20 13:22:45","alarmStartTime":"2025-11-20 11:32:15","alarmLevel":1,"ksbh":"429021001003","Alarmcode":"2953"}]
//            [35m2025-11-20 16:20:17[0;39m [34mINFO [0;39m [32mcom.mines.server.handler.TcpLoggingHandler

    /**
     * 自定义响应处理器
     */
    private static class ResponseHandler extends SimpleChannelInboundHandler<String> {
        private final CountDownLatch latch;
        private final String expectedDataId;
        private boolean success;
        private String responseDataId;
        private String errorMsg;

        public ResponseHandler(CountDownLatch latch, String expectedDataId) {
            this.latch = latch;
            this.expectedDataId = expectedDataId;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) {
            try {
                // 解析服务端响应
                Map<String, Object> response = JSON.parseObject(msg, Map.class);
                success = (Boolean) response.get("success");
                responseDataId = (String) response.get("dataId");

                // 如果是失败响应，记录错误信息
                if (!success) {
                    errorMsg = (String) response.get("message");
                }
            } catch (Exception e) {
                success = false;
                errorMsg = "响应解析失败: " + e.getMessage();
            } finally {
                latch.countDown(); // 通知等待线程
                ctx.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            success = false;
            errorMsg = "通信异常: " + cause.getMessage();
            latch.countDown();
            ctx.close();
        }

        // getter方法
        public boolean isSuccess() { return success; }
        public String getResponseDataId() { return responseDataId; }
        public String getErrorMsg() { return errorMsg; }
    }
}
