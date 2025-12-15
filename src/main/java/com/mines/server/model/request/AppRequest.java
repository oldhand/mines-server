package com.mines.server.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户端上报的外层请求格式
 */
@Data
@ApiModel(description = "客户端上报的外层请求封装")
public class AppRequest {
    @ApiModelProperty(value = "系统下发的应用标识", required = true, example = "APP2025001")
    private String appId; // 非空，系统下发

    @ApiModelProperty(value = "系统下发的服务标识（对应DataServiceId）", required = true, example = "SVC001")
    private String serviceId; // 非空，系统下发

    @ApiModelProperty(value = "调用方生成的全局唯一标识", required = true, example = "DATA20250520001")
    private String dataId; // 非空，调用方生成（全局唯一）

    @ApiModelProperty(value = "加密后的实时数据内容", required = true, example = "U2FsdGVkX1+...")
    private String data; // 非空，加密后的实时数据
}
