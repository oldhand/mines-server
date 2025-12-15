package com.mines.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mines.server.model.vo.SystemStatusVO;
import com.sun.management.OperatingSystemMXBean; // 注意导入扩展接口
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SystemService {

    @Value("${server.version:1.0.0}")
    private String serverVersion;

    private AtomicInteger connectionCount = new AtomicInteger(0);

    /**
     * 获取系统运行状态
     */
    public SystemStatusVO getSystemStatus() {
        SystemStatusVO status = new SystemStatusVO();
        status.setServerVersion(serverVersion);
        status.setRunStatus("RUNNING");
        status.setConnectionCount(connectionCount.get());

        // 获取扩展的操作系统MXBean（支持CPU使用率获取）
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // 注意：getSystemCpuLoad()返回的是最近1分钟的CPU使用率（0~1.0），转换为百分比
        status.setCpuUsage(osBean.getSystemCpuLoad() * 100);

        // 内存使用率计算（已使用内存 / 总内存）
        double usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        double totalMemory = Runtime.getRuntime().totalMemory();
        status.setMemoryUsage(usedMemory / totalMemory * 100);

        status.setStartTime(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
        return status;
    }

    public void incrementConnectionCount() {
        connectionCount.incrementAndGet();
    }

    public void decrementConnectionCount() {
        connectionCount.decrementAndGet();
    }
}
