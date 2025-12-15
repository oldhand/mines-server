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
import com.mines.server.model.vo.PowerStatVO;
import com.mines.server.model.vo.PersonStatVO;
import com.mines.server.service.StatisticsService;

import java.util.Date;

/**
 * 数据统计分析接口（文档扩展需求）
 */
@RestController
@RequestMapping("/api/statistics")
@Api(tags = "数据统计接口", description = "矿山数据汇总统计（用电量、人员分布等）")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 统计矿山用电量（按日/月）
     */
    @GetMapping("/power")
    @ApiOperation(value = "用电量统计", notes = "按矿山编码、时间范围和统计类型（日/月）查询用电量")
    public ResponseEntity<PowerStatVO> statPowerConsumption(
            @ApiParam(name = "enterpriseCode", value = "矿山编码", required = true)
            @RequestParam String enterpriseCode,
            @ApiParam(name = "startTime", value = "开始时间", required = true)
            @RequestParam Date startTime,
            @ApiParam(name = "endTime", value = "结束时间", required = true)
            @RequestParam Date endTime,
            @ApiParam(name = "statType", value = "统计类型", required = true, example = "DAY", allowableValues = "DAY, MONTH")
            @RequestParam String statType) {
        PowerStatVO result = statisticsService.statPower(enterpriseCode, startTime, endTime, statType);
        return ResponseEntity.ok(result);
    }

    /**
     * 统计人员位置分布
     */
    @GetMapping("/person-location")
    @ApiOperation(value = "人员位置统计", notes = "按矿山编码查询当前各区域人员分布")
    public ResponseEntity<PersonStatVO> statPersonLocation(
            @ApiParam(name = "enterpriseCode", value = "矿山编码", required = true)
            @RequestParam String enterpriseCode) {
        PersonStatVO result = statisticsService.statPersonLocation(enterpriseCode);
        return ResponseEntity.ok(result);
    }
}
