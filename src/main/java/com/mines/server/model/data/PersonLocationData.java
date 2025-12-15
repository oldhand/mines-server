package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 文档2.5.7：井下人员定位数据（dataType=07）
 */
@Data
@Entity
@Table(name = "t_person_location")
@ApiModel(description = "井下人员定位数据（dataType=07）")
public class PersonLocationData {
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

    @Column(length = 50, nullable = false)
    @ApiModelProperty(value = "班组名称", required = true, example = "掘进一班")
    private String teamName; // 班组名称

    @Column(length = 50, nullable = false)
    @ApiModelProperty(value = "职位", required = true, example = "矿工")
    private String position; // 职位

    @Column(nullable = false)
    @ApiModelProperty(value = "经度", required = true, example = "116.384520")
    private Double longitude; // 经度

    @Column(nullable = false)
    @ApiModelProperty(value = "纬度", required = true, example = "39.908823")
    private Double latitude; // 纬度

    @Column(length = 100, nullable = false)
    @ApiModelProperty(value = "位置描述", required = true, example = "2号工作面")
    private String location; // 位置描述

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "定位时间", required = true, example = "2025-05-20 14:30:00")
    private Date locateTime; // 定位时间
}
