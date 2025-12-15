package com.mines.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mines.server.model.data.*;
import com.mines.server.service.AlarmService;

import java.util.Date;
import java.util.List;

/**
 * 报警信息查询接口（文档扩展需求）
 */
@RestController
@RequestMapping("/api/alarm")
@Api(tags = "报警信息接口", description = "查询各类报警数据（超员、超时、限制区域等）")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    /**
     * 查询超员报警数据
     */
    @GetMapping("/overcrowd")
    @ApiOperation(value = "查询超员报警", notes = "按矿山编码和时间范围查询超员报警记录")
    public ResponseEntity<List<OvercrowdAlarmData>> getOvercrowdAlarms(
            @ApiParam(name = "enterpriseCode", value = "矿山编码", required = true)
            @RequestParam String enterpriseCode,
            @ApiParam(name = "startTime", value = "开始时间", required = true)
            @RequestParam Date startTime,
            @ApiParam(name = "endTime", value = "结束时间", required = true)
            @RequestParam Date endTime) {
        List<OvercrowdAlarmData> alarms = alarmService.getOvercrowdAlarms(enterpriseCode, startTime, endTime);
        return ResponseEntity.ok(alarms);
    }

    /**
     * 查询人员超时报警数据
     */
    @GetMapping("/overtime")
    @ApiOperation(value = "查询超时报警", notes = "按矿山编码和时间范围查询人员超时报警记录")
    public ResponseEntity<List<OvertimeAlarmData>> getOvertimeAlarms(
            @ApiParam(name = "enterpriseCode", value = "矿山编码", required = true)
            @RequestParam String enterpriseCode,
            @ApiParam(name = "startTime", value = "开始时间", required = true)
            @RequestParam Date startTime,
            @ApiParam(name = "endTime", value = "结束时间", required = true)
            @RequestParam Date endTime) {
        List<OvertimeAlarmData> alarms = alarmService.getOvertimeAlarms(enterpriseCode, startTime, endTime);
        return ResponseEntity.ok(alarms);
    }

    /**
     * 查询限制区域报警数据
     */
    @GetMapping("/restricted-area")
    @ApiOperation(value = "查询限制区域报警", notes = "按矿山编码和时间范围查询人员进入限制区域报警记录")
    public ResponseEntity<List<RestrictedAreaData>> getRestrictedAreaAlarms(
            @ApiParam(name = "enterpriseCode", value = "矿山编码", required = true)
            @RequestParam String enterpriseCode,
            @ApiParam(name = "startTime", value = "开始时间", required = true)
            @RequestParam Date startTime,
            @ApiParam(name = "endTime", value = "结束时间", required = true)
            @RequestParam Date endTime) {
        List<RestrictedAreaData> alarms = alarmService.getRestrictedAreaAlarms(enterpriseCode, startTime, endTime);
        return ResponseEntity.ok(alarms);
    }
}
