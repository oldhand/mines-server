package com.mines.server.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 扩展报警数据（dataType=1001）
 * 根据实际报文结构定义
 */
@Data
@Entity
@Table(name = "t_alert_data")
@ApiModel(description = "扩展报警数据（dataType=1001）")
public class AlertData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "429021001003")
    private String ksbh;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "报警开始时间", required = true, example = "2025-11-20 11:32:15")
    private Date alarmStartTime;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "报警结束时间", example = "2025-11-20 13:22:45")
    private Date alarmOverTime;

    @Column(nullable = false)
    @ApiModelProperty(value = "报警级别", required = true, example = "1")
    private Integer alarmLevel;

    @Column(nullable = false)
    @ApiModelProperty(value = "报警状态", required = true, example = "0")
    private Integer alarmStatus;

    @Column(nullable = false)
    @ApiModelProperty(value = "报警类型", required = true, example = "1")
    private Integer alarmType;

    @Column(length = 50)
    @ApiModelProperty(value = "关联对象", example = "417")
    private String relatedObj;

    @Column(length = 50)
    @JsonProperty("Alarmcode") // 显式映射JSON中的大写字段名
    @ApiModelProperty(value = "报警编码", example = "2953")
    private String alarmCode;

    @Column(length = 500)
    @ApiModelProperty(value = "报警描述", example = "蓝色告警：压力大于13.0MPa。")
    private String alarmDesc;
}