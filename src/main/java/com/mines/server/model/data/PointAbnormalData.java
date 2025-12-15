package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 文档2.5.6：测点异常数据（dataType=06）
 */
@Data
@Entity
@Table(name = "t_point_abnormal_data")
@ApiModel(description = "测点异常数据（dataType=06）")
public class PointAbnormalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 32, nullable = false)
    @ApiModelProperty(value = "测点编码", required = true, example = "POINT003")
    private String pointCode; // 测点编码

    @Column(nullable = false)
    @ApiModelProperty(value = "异常值", required = true, example = "35.8", notes = "超出正常范围的测点数值")
    private BigDecimal abnormalValue; // 异常值

    @Column(length = 2, nullable = false)
    @ApiModelProperty(value = "异常类型", required = true, example = "1", notes = "1:超上限,2:超下限")
    private String abnormalType; // 异常类型（1:超上限,2:超下限）

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "发生时间", required = true, example = "2025-05-20 16:00:00")
    private Date happenTime; // 发生时间
}
