package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 文档2.5.9：超员报警数据（dataType=09）
 */
@Data
@Entity
@Table(name = "t_overcrowd_alarm")
@ApiModel(description = "超员报警数据（dataType=09）")
public class OvercrowdAlarmData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 100, nullable = false)
    @ApiModelProperty(value = "区域名称", required = true, example = "井下作业区A")
    private String areaName; // 区域名称

    @Column(nullable = false)
    @ApiModelProperty(value = "限制人数", required = true, example = "10", notes = "该区域允许的最大人数")
    private Integer limitNum; // 限制人数

    @Column(nullable = false)
    @ApiModelProperty(value = "当前人数", required = true, example = "15", notes = "该区域实际的当前人数（已超限制）")
    private Integer currentNum; // 当前人数

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "报警时间", required = true, example = "2025-05-20 14:30:00")
    private Date alarmTime; // 报警时间
}
