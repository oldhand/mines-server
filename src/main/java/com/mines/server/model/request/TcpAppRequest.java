package com.mines.server.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Map;

/**
 * TCP请求模型（支持多接口标识）
 */
@Data
@ApiModel(description = "TCP请求模型（包含接口标识）")
public class TcpAppRequest extends AppRequest {
    @ApiModelProperty(value = "接口标识", required = true, example = "REALTIME_REPORT",
            notes = "REALTIME_REPORT:实时数据上报, SYSTEM_VERSION:版本查询, SYSTEM_STATUS:状态查询等")
    private String interfaceCode;

    @ApiModelProperty(value = "请求参数集合", notes = "用于存储非加密的业务参数，如查询条件等")
    private Map<String, Object> params;

    /**
     * 获取参数值（字符串形式），供Handler调用
     * @param key 参数名
     * @return 参数值字符串，若不存在则返回null
     */
    public String getParam(String key) {
        if (params == null || params.get(key) == null) {
            return null;
        }
        return String.valueOf(params.get(key));
    }
}