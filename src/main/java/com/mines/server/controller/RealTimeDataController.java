package com.mines.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mines.server.model.request.AppRequest;
import com.mines.server.model.response.AppResponse;
import com.mines.server.service.RealTimeDataService;

/**
 * 实时数据上报接口（文档2.3/2.4要求）
 */
@Slf4j
@RestController
@RequestMapping("/api/realtime-data")
@Api(tags = "实时数据接口", description = "处理加密后的实时数据上报（HTTP方式）")
public class RealTimeDataController {

    @Autowired
    private RealTimeDataService realTimeDataService;

    /**
     * 上报实时数据（HTTP接口）
     */
    @PostMapping("/report")
    @ApiOperation(value = "上报实时数据", notes = "客户端通过HTTP POST上报加密后的实时数据，支持多种数据类型（01~12）")
    public ResponseEntity<AppResponse> report(
            @ApiParam(name = "appRequest", value = "外层请求封装（含加密数据）", required = true)
            @RequestBody AppRequest appRequest) throws Exception {
        AppResponse response = realTimeDataService.processData(appRequest);
        return ResponseEntity.ok(response);
    }
}
