package com.mines.server.model.request;

import com.mines.server.model.data.RiskObjectDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(description = "风险对象数据上报请求")
public class RiskObjectRequest {
    @NotNull(message = "objectList不能为空")
    @Size(min = 1, message = "objectList至少包含一条数据")
    @ApiModelProperty(value = "风险对象列表（新增/修改/删除的数据）", required = true)
    private List<RiskObjectDTO> objectList;
}
