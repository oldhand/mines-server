package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 文档2.5.10：限制区域报警数据（dataType=10）
 */
@Data
@Entity
@Table(name = "t_restricted_area_alarm")
@ApiModel(description = "限制区域报警数据（dataType=10）")
public class RestrictedAreaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 18, nullable = false)
    @ApiModelProperty(value = "人员身份证号", required = true, example = "110101199001011234")
    private String idCard; // 身份证号

    @Column(length = 50, nullable = false)
    @ApiModelProperty(value = "人员姓名", required = true, example = "张三")
    private String personName; // 人员姓名

    @Column(length = 100, nullable = false)
    @ApiModelProperty(value = "限制区域名称", required = true, example = "爆破危险区")
    private String areaName; // 限制区域名称

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "进入时间", required = true, example = "2025-05-20 16:45:00")
    private Date enterTime; // 进入时间
}
