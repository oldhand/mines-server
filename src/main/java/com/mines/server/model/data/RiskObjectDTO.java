package com.mines.server.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "风险对象数据详情")
public class RiskObjectDTO {

    @NotNull(message = "id不能为空")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "id必须是36位UUID")
    @ApiModelProperty(value = "唯一编码（36位UUID）", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @ApiModelProperty(value = "安全风险分析对象所属部门负责人姓名", required = true, example = "张安全")
    private String riskLiablePerson;

    @ApiModelProperty(value = "矿山编号", required = true, example = "MINE001")
    private String mineCode;

    @ApiModelProperty(value = "风险对象名称", required = true, example = "矿井通风系统")
    private String riskObjectName;

    @ApiModelProperty(value = "风险等级（1-重大危险；2-较大风险；3-一般风险；4-低风险）",
            required = true, example = "1")
    private Integer riskLevel;

    @NotNull(message = "deleted不能为空")
    @Pattern(regexp = "^[01]$", message = "deleted必须为0（未删除）或1（已删除）")
    @ApiModelProperty(value = "删除状态（0-未删除；1-已删除）", required = true, example = "0")
    private String deleted;

    @ApiModelProperty(value = "创建时间（格式：yyyy-MM-dd HH:mm:ss）",
            required = true, example = "2023-06-15 09:30:45")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "创建人", required = true, example = "admin")
    private String createBy;

    @NotNull(message = "updateDate不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间（格式：yyyy-MM-dd HH:mm:ss）",
            required = true, example = "2023-06-15 14:20:10")
    private LocalDateTime updateDate;

    @NotNull(message = "updateBy不能为空")
    @ApiModelProperty(value = "修改人", required = true, example = "admin")
    private String updateBy;
}
