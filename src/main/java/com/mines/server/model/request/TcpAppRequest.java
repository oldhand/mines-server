package com.mines.server.model.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TCP请求模型（支持多接口标识）
 */
@Data
@ApiModel(description = "TCP请求模型（包含接口标识）")
public class TcpAppRequest extends AppRequest {
    @ApiModelProperty(value = "接口标识", required = true, example = "REALTIME_REPORT",
            notes = "REALTIME_REPORT:实时数据上报, SYSTEM_VERSION:版本查询, SYSTEM_STATUS:状态查询等")
    private String interfaceCode;
}
