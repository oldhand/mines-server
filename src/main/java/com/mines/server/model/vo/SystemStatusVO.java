package com.mines.server.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel(description = "系统运行状态信息")
public class SystemStatusVO {
    @ApiModelProperty(value = "服务版本", required = true)
    private String serverVersion;

    @ApiModelProperty(value = "运行状态（RUNNING/STOPPED）", required = true)
    private String runStatus;

    @ApiModelProperty(value = "当前连接数", required = true)
    private Integer connectionCount;

    @ApiModelProperty(value = "CPU使用率（%）", required = true)
    private double cpuUsage;

    @ApiModelProperty(value = "内存使用率（%）", required = true)
    private double memoryUsage;

    @ApiModelProperty(value = "服务启动时间", required = true)
    private Date startTime;
}
