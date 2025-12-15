package com.mines.server.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 响应中的错误信息
 */
@Data
@ApiModel(description = "响应中的错误详情")
public class ErrorDTO {
    @ApiModelProperty(value = "错误码", required = true, example = "400")
    private String code; // 错误码

    @ApiModelProperty(value = "错误标识", required = true, example = "INVALID_DATA_TYPE")
    private String id; // 错误标识

    @ApiModelProperty(value = "错误描述", required = true, example = "不支持的数据类型：99")
    private String message; // 错误描述
}
