package com.mines.server.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 设备静态信息实体类（对应地下矿V6规范中的设备基础数据）
 * 存储设备唯一标识、名称、数据类型、所属区域等静态属性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "t_device",  // 数据库表名：t_前缀表示业务表
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "deviceId")  // 设备ID唯一约束
        }
)
public class Device {

    /**
     * 设备唯一标识（如"tempSensor001"）
     * 规则：设备类型缩写+编号，长度不超过50字符
     */
    @Id
    @NotBlank(message = "设备ID不能为空")
    @Length(max = 50, message = "设备ID长度不能超过50字符")
    @Column(nullable = false, length = 50)
    private String deviceId;

    /**
     * 设备名称（如"温度传感器-井下1号区"）
     * 需明确设备用途和位置，长度不超过100字符
     */
    @NotBlank(message = "设备名称不能为空")
    @Length(max = 100, message = "设备名称长度不能超过100字符")
    @Column(nullable = false, length = 100)
    private String deviceName;

    /**
     * 数据类型（对应V6规范的12类数据）
     * 1-温度；2-湿度；3-瓦斯浓度；...；12-设备状态
     */
    @Min(value = 1, message = "数据类型必须≥1")
    @Max(value = 12, message = "数据类型必须≤12（V6规范定义12类数据）")
    @Column(nullable = false)
    private Integer dataType;

    /**
     * 所属区域ID（如"area001-井下1号区"）
     * 关联矿下区域划分，为空表示未分配区域
     */
    @Length(max = 50, message = "区域ID长度不能超过50字符")
    @Column(length = 50)
    private String areaId;

    /**
     * 简化构造方法（用于快速创建设备对象）
     */
    public Device(String deviceId, String deviceName, Integer dataType) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.dataType = dataType;
    }
}
