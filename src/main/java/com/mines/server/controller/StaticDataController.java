package com.mines.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mines.server.model.data.StaticData;
import com.mines.server.service.StaticDataService;

/**
 * 静态数据填报接口（文档2.2要求）
 */
@RestController
@RequestMapping("/api/static-data")
@Api(tags = "静态数据接口", description = "实时数据上报前需完成的基础信息填报与查询")
public class StaticDataController {

    @Autowired
    private StaticDataService staticDataService;

    /**
     * 提交静态数据
     */
    @PostMapping("/submit")
    @ApiOperation(value = "提交静态数据", notes = "首次填报矿山基础信息，矿山编码唯一")
    public ResponseEntity<String> submit(
            @ApiParam(name = "staticData", value = "静态数据实体", required = true)
            @RequestBody StaticData staticData) {
        staticDataService.saveStaticData(staticData);
        return ResponseEntity.ok("静态数据提交成功");
    }

    /**
     * 更新静态数据
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新静态数据", notes = "修改已存在的矿山基础信息，通过矿山编码匹配")
    public ResponseEntity<String> update(
            @ApiParam(name = "staticData", value = "静态数据实体（含已存在的矿山编码）", required = true)
            @RequestBody StaticData staticData) {
        staticDataService.updateStaticData(staticData);
        return ResponseEntity.ok("静态数据更新成功");
    }

    /**
     * 查询静态数据
     */
    @GetMapping("/query")
    @ApiOperation(value = "查询静态数据", notes = "通过矿山编码查询已填报的基础信息")
    public ResponseEntity<StaticData> query(
            @ApiParam(name = "enterpriseCode", value = "矿山编码", required = true, example = "KS2025001")
            @RequestParam String enterpriseCode) {
        StaticData staticData = staticDataService.getByEnterpriseCode(enterpriseCode);
        return ResponseEntity.ok(staticData);
    }
}
