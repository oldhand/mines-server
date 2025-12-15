package com.mines.server.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Map;

@Data
@ApiModel(description = "人员位置分布统计结果")
public class PersonStatVO {
    @ApiModelProperty(value = "矿山编码", required = true)
    private String enterpriseCode;

    @ApiModelProperty(value = "总人数", required = true)
    private Integer totalPerson;

    @ApiModelProperty(value = "区域人员分布（区域名称 -> 人数）", required = true)
    private Map<String, Long> locationStats;
}
