package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 文档2.5.5：测点统计数据（dataType=05）
 */
@Data
@Entity
@Table(name = "t_point_stat_data")
@ApiModel(description = "测点统计数据（dataType=05）")
public class PointStatData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 32, nullable = false)
    @ApiModelProperty(value = "测点编码", required = true, example = "POINT001")
    private String pointCode; // 测点编码

    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "统计类型", required = true, example = "1", notes = "1:时,2:日,3:月")
    private String statType; // 统计类型（1:时,2:日,3:月）

    @Column(nullable = false)
    @ApiModelProperty(value = "平均值", required = true, example = "25.67", notes = "数值型指标的统计平均值")
    private BigDecimal avgValue; // 平均值

    @Column(nullable = false)
    @ApiModelProperty(value = "最大值", required = true, example = "30.20", notes = "数值型指标的统计最大值")
    private BigDecimal maxValue; // 最大值

    @Column(nullable = false)
    @ApiModelProperty(value = "最小值", required = true, example = "20.15", notes = "数值型指标的统计最小值")
    private BigDecimal minValue; // 最小值

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "统计时间", required = true, example = "2025-05-20 23:59:59")
    private Date statTime; // 统计时间
}
