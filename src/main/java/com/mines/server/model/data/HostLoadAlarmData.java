package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 文档2.5.3：主机负载告警数据（dataType=03）
 */
@Data
@Entity
@Table(name = "t_host_load_alarm")
@ApiModel(description = "主机负载告警数据（dataType=03）")
public class HostLoadAlarmData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 50, nullable = false)
    @ApiModelProperty(value = "主机名称", required = true, example = "监测服务器01")
    private String hostName; // 主机名称

    @Column(length = 20, nullable = false)
    @ApiModelProperty(value = "主机IP地址", required = true, example = "192.168.1.100")
    private String hostIp; // 主机IP

    @Column(length = 2, nullable = false)
    @ApiModelProperty(value = "告警类型", required = true, example = "1", notes = "1:CPU,2:内存")
    private String alarmType; // 告警类型（1:CPU,2:内存）

    @Column(nullable = false)
    @ApiModelProperty(value = "负载值", required = true, example = "85.5", notes = "百分比，0~100")
    private Double loadValue; // 负载值（%）

    @Column(length = 2, nullable = false)
    @ApiModelProperty(value = "告警级别", required = true, example = "2", notes = "1:警告,2:严重")
    private String alarmLevel; // 告警级别（1:警告,2:严重）

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "告警时间", required = true, example = "2025-05-20 16:00:00")
    private Date alarmTime; // 告警时间
}
