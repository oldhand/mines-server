package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 文档2.5.8：超时报警数据（dataType=08）
 */
@Data
@Entity
@Table(name = "t_overtime_alarm")
@ApiModel(description = "超时报警数据（dataType=08）")
public class OvertimeAlarmData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 18, nullable = false)
    @ApiModelProperty(value = "人员身份证号", required = true, example = "110101199001011234")
    private String idCard; // 身份证号

    @Column(length = 50, nullable = false)
    @ApiModelProperty(value = "人员姓名", required = true, example = "李四")
    private String personName; // 人员姓名

    @Column(nullable = false)
    @ApiModelProperty(value = "超时小时数", required = true, example = "2", notes = "超出规定工作时长的小时数")
    private Integer overtimeHours; // 超时小时数

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "报警时间", required = true, example = "2025-05-20 21:30:00")
    private Date alarmTime; // 报警时间
}
