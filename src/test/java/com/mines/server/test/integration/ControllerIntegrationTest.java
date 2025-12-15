package com.mines.server.test.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void initTestData() throws Exception {
        // 初始化测试设备
        String deviceJson = "{" +
                "\"deviceId\":\"testDevice001\"," +
                "\"deviceName\":\"温度传感器-001\"," +
                "\"dataType\":1," +
                "\"areaId\":\"area001\"" +
                "}";
        mockMvc.perform(post("/api/static/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isOk());
    }

    // 健康检查接口测试
    @Test
    public void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    // 静态设备查询-异常场景（设备不存在）
    @Test
    public void testQueryDevice_NotFound() throws Exception {
        mockMvc.perform(get("/api/static/device/nonExist001"))
                .andExpect(status().isNotFound());
    }

    // 加密数据上报-异常场景（无效加密串）
    @Test
    public void testEncryptedDataReport_Invalid() throws Exception {
        mockMvc.perform(post("/api/report/encrypted")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid_encrypted_data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }
}
