package com.mines.server.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mines.server.model.request.AppRequest;
import com.mines.server.model.request.RealTimeData;
import com.mines.server.util.AesUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 针对 /api/realtime-data/report 接口的HTTP测试（基于RealTimeData模型）
 * 特性：不依赖WebMvcTest、H2、Mock，直接访问真实部署的应用
 * 前提：应用已启动，接口可访问（如 http://localhost:8080）
 */
public class StaticDataControllerApiTest {

    // 应用基础地址（根据实际部署环境修改）
    private static final String APP_BASE_URL = "http://127.0.0.1:30180";
    // 目标接口URL
    private static final String REPORT_API = APP_BASE_URL + "/api/realtime-data/report";

    // AES加密参数（必须与服务端一致，否则解密失败）
    private static final String AES_KEY = "1234567890ABCDEF"; // 16字节密钥（AES-128）
    private static final String AES_IV = "0000000000000000";  // 16字节IV向量

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private String testDataId; // 用于记录测试数据ID，测试后清理

    @Before
    public void init() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
        testDataId = null;
    }
    /**
     * 场景1：正常上报（所有必填字段完整，格式正确）
     * 验证：接口返回200 + 成功标识 + 数据ID
     */
    @Test
    public void testReport_Success() throws Exception {
        // 1. 构建符合要求的RealTimeData对象
        RealTimeData realTimeData = new RealTimeData();
        realTimeData.setDataId("DATA_" + System.currentTimeMillis()); // 全局唯一
        realTimeData.setEnterpriseCode("KS2025001"); // 矿山编码
        realTimeData.setEnterpriseType("DXK"); // 固定值
        realTimeData.setEnterpriseName("测试煤矿"); // 可选
        realTimeData.setGatewayCode("GW2025001"); // 网关编码
        realTimeData.setCollectTime("2025-11-13 15:30:00"); // 正确格式
        realTimeData.setIsConnectDataSource(true); // 连通状态
        realTimeData.setReportType("report"); // 正常上报
        realTimeData.setDataType("01"); // 数据类型（01~12）

        // 构造datas（示例：dataType=01对应温度数据，包含deviceId和value）
        List<Map<String, Object>> datas = new ArrayList<>();
        Map<String, Object> dataItem = new HashMap<>();
        dataItem.put("deviceId", "DEV001");
        dataItem.put("temperature", 25.6);
        datas.add(dataItem);
        realTimeData.setDatas(datas);
        System.out.println("实时数据：" + dataItem.toString());


        // 2. 将对象序列化为JSON字符串（明文）
        String plainJson = objectMapper.writeValueAsString(realTimeData);
        System.out.println("原始明文：" + plainJson);

        // 3. 使用AesUtils加密（关键步骤：与服务端加密算法一致）
        System.out.println(" secretKey : " + AES_KEY + ",iv: " + AES_IV);
        String encryptedData = AesUtils.encrypt(plainJson, AES_KEY, AES_IV);
        System.out.println("加密后数据：" + encryptedData);

        AppRequest AppRequest = new AppRequest();
        AppRequest.setAppId("3c9a6868a6d74e348708ad3f0c15c25b");
        AppRequest.setServiceId("c6314bc9888b4134bc9e6b989dd37679"); // 假设的服务ID
        AppRequest.setDataId(realTimeData.getDataId());
        AppRequest.setData(encryptedData);

        System.out.println("AppRequest：" + AppRequest.toString());

        // 2. 构建HTTP请求（POST + JSON）
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(
                objectMapper.writeValueAsString(AppRequest),
                headers
        );

        // 3. 发送请求并获取响应
        ResponseEntity<Map> response = restTemplate.postForEntity(
                REPORT_API,
                request,
                Map.class
        );

        // 4. 验证响应
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        System.out.println("responseBody：" + responseBody.toString());
        assertNotNull(responseBody);
        assertTrue((Boolean) responseBody.get("success"));
        testDataId = responseBody.get("dataId").toString(); // 记录ID用于清理
        assertNotNull("响应应返回dataId", testDataId);
    }

}
