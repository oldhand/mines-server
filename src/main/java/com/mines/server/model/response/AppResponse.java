package com.mines.server.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务端统一响应格式
 */
@Data
@ApiModel(description = "服务端统一响应格式")
public class AppResponse {
    @ApiModelProperty(value = "处理结果", required = true, example = "true", notes = "true=成功，false=失败")
    private Boolean success; // 非空，true=成功，false=失败

    @ApiModelProperty(value = "数据标识（与请求dataId一致）", required = true, example = "DATA20250520001")
    private String dataId; // 非空，与请求dataId一致

    @ApiModelProperty(value = "错误信息（失败时非空）")
    private ErrorDTO error; // 失败时非空

    @ApiModelProperty(value = "返回数据（成功时可能非空）")
    private Object data; // 成功时返回的数据

    // 成功响应
    public static AppResponse success(String dataId) {
        AppResponse response = new AppResponse();
        response.setSuccess(true);
        response.setDataId(dataId);
        return response;
    }

    // 失败响应
    public static AppResponse fail(String code, String errorId, String message, String dataId) {
        AppResponse response = new AppResponse();
        response.setSuccess(false);
        response.setDataId(dataId);
        ErrorDTO error = new ErrorDTO();
        error.setCode(code);
        error.setId(errorId);
        error.setMessage(message);
        response.setError(error);
        return response;
    }
}
