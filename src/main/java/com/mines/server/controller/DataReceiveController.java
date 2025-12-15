package com.mines.server.controller;

import com.mines.server.model.request.AppRequest;
import com.mines.server.model.response.AppResponse;
import com.mines.server.service.RealTimeDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@Api(tags = "实时数据接口", description = "处理加密后的实时数据上报（HTTP方式）")
public class DataReceiveController {
    @Autowired
    private RealTimeDataService realTimeDataService;

    /**
     * 接收风险对象数据
     */
    @PostMapping("/receive/receiveObjectList")
    @ApiOperation(value = "接收风险对象数据")
    public ResponseEntity<AppResponse> receiveObjectList(
            @ApiParam(name = "appRequest", value = "外层请求封装（含加密数据）", required = true)
            @RequestBody AppRequest appRequest) throws Exception {
        AppResponse response = realTimeDataService.processData(appRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/receive/receiveImgtList")
    @ApiOperation(value = "接收风险空间分布图数据")
    public ResponseEntity<AppResponse> receiveImgtList(
            @ApiParam(name = "appRequest", value = "外层请求封装（含加密数据）", required = true)
            @RequestBody AppRequest appRequest) throws Exception {
        AppResponse response = realTimeDataService.processData(appRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/receive/receiveUnitList")
    @ApiOperation(value = "接收风险单元数据")
    public ResponseEntity<AppResponse> receiveUnitList(
            @ApiParam(name = "appRequest", value = "外层请求封装（含加密数据）", required = true)
            @RequestBody AppRequest appRequest) throws Exception {
        AppResponse response = realTimeDataService.processData(appRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/receive/receiveEventList")
    @ApiOperation(value = "接收风险事件数据")
    public ResponseEntity<AppResponse> receiveEventList(
            @ApiParam(name = "appRequest", value = "外层请求封装（含加密数据）", required = true)
            @RequestBody AppRequest appRequest) throws Exception {
        AppResponse response = realTimeDataService.processData(appRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/receive/receiveControlMeasuresList")
    @ApiOperation(value = "接收管控措施数据 ")
    public ResponseEntity<AppResponse> receiveControlMeasuresList(
            @ApiParam(name = "appRequest", value = "外层请求封装（含加密数据）", required = true)
            @RequestBody AppRequest appRequest) throws Exception {
        AppResponse response = realTimeDataService.processData(appRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/receive/receiveMeasuresTaskList")
    @ApiOperation(value = "接收隐患排查任务数据")
    public ResponseEntity<AppResponse> receiveMeasuresTaskList(
            @ApiParam(name = "appRequest", value = "外层请求封装（含加密数据）", required = true)
            @RequestBody AppRequest appRequest) throws Exception {
        AppResponse response = realTimeDataService.processData(appRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/receive/receiveMeasuresTaskRecordList")
    @ApiOperation(value = "接收隐患排查记录")
    public ResponseEntity<AppResponse> receiveMeasuresTaskRecordList(
            @ApiParam(name = "appRequest", value = "外层请求封装（含加密数据）", required = true)
            @RequestBody AppRequest appRequest) throws Exception {
        AppResponse response = realTimeDataService.processData(appRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/receive/receiveDangerInvestigationList")
    @ApiOperation(value = "接收隐患信息数据")
    public ResponseEntity<AppResponse> receiveDangerInvestigationList(
            @ApiParam(name = "appRequest", value = "外层请求封装（含加密数据）", required = true)
            @RequestBody AppRequest appRequest) throws Exception {
        AppResponse response = realTimeDataService.processData(appRequest);
        return ResponseEntity.ok(response);
    }
}
