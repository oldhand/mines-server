package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 文档2.5.4：测点实时数据（dataType=04）
 */
@Data
@Entity
@Table(name = "t_point_real_data")
@ApiModel(description = "测点实时数据（dataType=04）")
public class PointRealData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 32, nullable = false)
    @ApiModelProperty(value = "测点编码", required = true, example = "POINT002")
    private String pointCode; // 测点编码

    @Column(length = 100, nullable = false)
    @ApiModelProperty(value = "测点名称", required = true, example = "井下温度传感器")
    private String pointName; // 测点名称

    @Column(nullable = false)
    @ApiModelProperty(value = "测点值", required = true, example = "26.5", notes = "传感器采集的实时数值")
    private BigDecimal pointValue; // 测点值

    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "单位", required = true, example = "℃", notes = "测点值的计量单位")
    private String unit; // 单位

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "采集时间", required = true, example = "2025-05-20 14:30:00")
    private Date collectTime; // 采集时间
}
