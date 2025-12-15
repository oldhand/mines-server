package com.mines.server.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.Map;

@Data
@ApiModel(description = "用电量统计结果")
public class PowerStatVO {
    @ApiModelProperty(value = "矿山编码", required = true)
    private String enterpriseCode;

    @ApiModelProperty(value = "统计开始时间", required = true)
    private Date startTime;

    @ApiModelProperty(value = "统计结束时间", required = true)
    private Date endTime;

    @ApiModelProperty(value = "统计类型（DAY/MONTH）", required = true)
    private String statType;

    @ApiModelProperty(value = "统计结果（时间key -> 用电量）", required = true)
    private Map<String, Double> powerStats; // key: 日期（yyyy-MM-dd）或月份（yyyy-MM）
}
