package com.mines.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mines.server.model.vo.SystemStatusVO;
import com.mines.server.service.SystemService;

/**
 * 系统管理接口（文档扩展需求）
 */
@RestController
@RequestMapping("/api/system")
@Api(tags = "系统管理接口", description = "系统状态查询、版本信息等")
public class SystemController {

    @Value("${server.version:1.0.0}")
    private String serverVersion;

    @Autowired
    private SystemService systemService;

    /**
     * 查询服务版本信息
     */
    @GetMapping("/version")
    @ApiOperation(value = "查询服务版本", notes = "获取当前服务的版本号")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok(serverVersion);
    }

    /**
     * 查询系统运行状态
     */
    @GetMapping("/status")
    @ApiOperation(value = "查询系统状态", notes = "获取服务运行状态、连接数等信息")
    public ResponseEntity<SystemStatusVO> getSystemStatus() {
        SystemStatusVO status = systemService.getSystemStatus();
        return ResponseEntity.ok(status);
    }
}
