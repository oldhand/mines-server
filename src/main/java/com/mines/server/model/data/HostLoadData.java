package com.mines.server.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 文档2.5.2：主机负载数据（dataType=02）
 */
@Data
@Entity
@Table(name = "t_host_load_data")
@ApiModel(description = "主机负载数据（dataType=02）")
public class HostLoadData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(length = 28, nullable = false)
    @ApiModelProperty(value = "矿山编码", required = true, example = "KS2025001")
    private String ksbh; // 矿山编码

    @Column(length = 50, nullable = false)
    @ApiModelProperty(value = "主机名称", required = true, example = "监测服务器01")
    private String hostName; // 主机名称

    @Column(length = 20, nullable = false)
    @ApiModelProperty(value = "主机IP地址", required = true, example = "192.168.1.100")
    private String hostIp; // 主机IP

    @Column(nullable = false)
    @ApiModelProperty(value = "CPU负载", required = true, example = "65.2", notes = "百分比，0~100")
    private Double cpuLoad; // CPU负载（%）

    @Column(nullable = false)
    @ApiModelProperty(value = "内存负载", required = true, example = "72.5", notes = "百分比，0~100")
    private Double memLoad; // 内存负载（%）

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "数据产生时间", required = true, example = "2025-05-20 15:30:00")
    private Date dataTime; // 数据产生时间
}
