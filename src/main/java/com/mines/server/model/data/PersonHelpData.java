package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 文档2.5.11：人员求救数据（dataType=11）
 */
@Data
@Entity
@Table(name = "t_person_help")
@ApiModel(description = "人员求救数据（dataType=11）")
public class PersonHelpData {
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
    @ApiModelProperty(value = "求救位置", required = true, example = "3号巷道100米处")
    private String location; // 求救位置

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "求救时间", required = true, example = "2025-05-20 15:30:00")
    private Date helpTime; // 求救时间
}
