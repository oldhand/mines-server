package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 文档2.5.1：企业用电量数据（dataType=01）
 */
@Data
@Entity
@Table(name = "t_power_data")
@ApiModel(description = "企业用电量数据（dataType=01）")
public class PowerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码（与enterpriseCode一致）", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 18, nullable = false)
    @ApiModelProperty(value = "电网用电编号", required = true, example = "DY2025001")
    private String powerCode; // 电网用电编号

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @ApiModelProperty(value = "用电日期", required = true, example = "2025-05-20")
    private Date powerDate; // 用电日期（yyyy-MM-dd）

    @Column(precision = 13, scale = 2, nullable = false)
    @ApiModelProperty(value = "用电量（kwh）", required = true, example = "1234.56", notes = "保留2位小数")
    private BigDecimal powerConsumption; // 用电量（kwh，保留2位小数）

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "数据产生时间", required = true, example = "2025-05-20 23:59:59")
    private Date dataTime; // 数据产生时间
}
