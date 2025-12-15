package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 文档2.5.12：分站运行数据（dataType=12）
 */
@Data
@Entity
@Table(name = "t_substation_run_data")
@ApiModel(description = "分站运行数据（dataType=12）")
public class SubstationRunData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 32, nullable = false)
    @ApiModelProperty(value = "分站编码", required = true, example = "SUB001")
    private String substationCode; // 分站编码

    @Column(length = 50, nullable = false)
    @ApiModelProperty(value = "分站名称", required = true, example = "井下一号分站")
    private String substationName; // 分站名称

    @Column(length = 2, nullable = false)
    @ApiModelProperty(value = "运行状态", required = true, example = "1", notes = "1:正常,0:异常")
    private String runStatus; // 运行状态（1:正常,0:异常）

    @Column(nullable = false)
    @ApiModelProperty(value = "信号强度", required = true, example = "90", notes = "百分比，0~100")
    private Integer signalStrength; // 信号强度（%）

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "上报时间", required = true, example = "2025-05-20 15:00:00")
    private Date reportTime; // 上报时间
}
