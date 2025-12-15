package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

/**
 * 静态数据（文档2.2：实时数据上报前需填报的基础信息）
 */
@Data
@Entity
@Table(name = "t_static_data")
@ApiModel(description = "矿山静态基础信息")
public class StaticData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true) // 隐藏自增主键，无需前端传递
    private Long id;

    @Column(length = 28, nullable = false, unique = true)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001",
            notes = "全局唯一，由系统分配")
    private String enterpriseCode;

    @Column(length = 100, nullable = false)
    @ApiModelProperty(value = "矿山名称", required = true, example = "XX煤矿")
    private String enterpriseName;

    @Column(length = 200, nullable = false)
    @ApiModelProperty(value = "矿山地址", required = true, example = "XX省XX市XX区")
    private String address;

    @Column(length = 20, nullable = false)
    @ApiModelProperty(value = "联系电话", required = true, example = "13800138000")
    private String contactPhone;

    @Column(length = 50, nullable = false)
    @ApiModelProperty(value = "法人", required = true, example = "张三")
    private String legalPerson;

    @Column(length = 2, nullable = false)
    @ApiModelProperty(value = "生产状态", required = true, example = "1",
            notes = "1:生产,0:停产")
    private String productionStatus;
}
