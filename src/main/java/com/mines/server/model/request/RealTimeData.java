package com.mines.server.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 解密后的实时数据主体（data字段内容）
 */
@Data
@ApiModel(description = "解密后的实时数据主体内容")
public class RealTimeData {
    @ApiModelProperty(value = "全局唯一数据标识（与外层dataId一致）", required = true, example = "DATA20250520001")
    private String dataId; // 非空，全局唯一

    @ApiModelProperty(value = "矿山编码（关联静态数据）", required = true, example = "KS2025001")
    private String enterpriseCode; // 非空，矿山编码

    @ApiModelProperty(value = "企业类型（固定值DXK）", required = true, example = "DXK")
    private String enterpriseType; // 非空，固定值DXK

    @ApiModelProperty(value = "矿山名称（可选）", example = "XX煤矿")
    private String enterpriseName; // 可选，矿山名称

    @ApiModelProperty(value = "网关编码", required = true, example = "GW2025001")
    private String gatewayCode; // 非空，网关编码

    @ApiModelProperty(value = "数据采集时间", required = true, example = "2025-05-20 14:30:00", notes = "格式：yyyy-MM-dd HH:mm:ss")
    private String collectTime; // 非空，格式yyyy-MM-dd HH:mm:ss

    @ApiModelProperty(value = "数据源连通状态", required = true, example = "true", notes = "true=连通，false=断开")
    private Boolean isConnectDataSource; // 非空，数据源连通性

    @ApiModelProperty(value = "上报类型", required = true, example = "report", notes = "report=正常上报，continues=断点续传")
    private String reportType; // 非空，report/continues（断点续传）

    @ApiModelProperty(value = "数据类型编码", required = true, example = "01", notes = "01~12，对应不同数据类型")
    private String dataType; // 非空，01~12（数据类型）

    @ApiModelProperty(value = "具体数据集合（根据dataType匹配对应实体）", required = true)
    private List<?> datas; // 非空，对应dataType的指标数据集合
}
