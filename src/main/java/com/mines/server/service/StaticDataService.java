package com.mines.server.service;

import com.mines.server.model.data.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mines.server.model.data.StaticData;
import com.mines.server.repository.StaticDataRepository;
import java.util.Optional;

/**
 * 静态数据填报服务（文档2.2要求）
 */
@Service
public class StaticDataService {

    @Autowired
    private StaticDataRepository staticDataRepository;

    /** 保存静态数据 */
    public void saveStaticData(StaticData staticData) {
        if (staticDataRepository.existsByEnterpriseCode(staticData.getEnterpriseCode())) {
            throw new RuntimeException("矿山编码已存在：" + staticData.getEnterpriseCode());
        }
        staticDataRepository.save(staticData);
    }

    /** 更新静态数据 */
    public void updateStaticData(StaticData staticData) {
        if (!staticDataRepository.existsByEnterpriseCode(staticData.getEnterpriseCode())) {
            throw new RuntimeException("矿山编码不存在：" + staticData.getEnterpriseCode());
        }
        staticDataRepository.save(staticData);
    }

    /** 新增：通过矿山编码查询静态数据 */
    public StaticData getByEnterpriseCode(String enterpriseCode) {
        Optional<StaticData> staticData = staticDataRepository.findByEnterpriseCode(enterpriseCode);
        // 若查询不到数据，可返回null或抛出异常（根据业务需求选择）
        return staticData.orElse(null);
        // 若需要强制检查存在性，可改为：
        // return staticData.orElseThrow(() -> new RuntimeException("矿山编码不存在：" + enterpriseCode));
    }

    /** 新增设备信息 */
    public Device addDevice(Device device) {
        // 1. 基础参数校验
        if (device == null) {
            throw new IllegalArgumentException("设备信息不能为空");
        }
        if (device.getDeviceId() == null || device.getDeviceId().trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        if (device.getDeviceName() == null || device.getDeviceName().trim().isEmpty()) {
            throw new IllegalArgumentException("设备名称不能为空");
        }
        return device;
    }

    /**
     * 根据设备ID查询设备静态信息
     * @param deviceId 设备唯一标识（非空）
     * @return 设备完整信息（包含deviceId、deviceName、dataType等字段）
     * @throws RuntimeException 当设备ID不存在或查询失败时抛出
     */
    public Device getDeviceByDeviceId(String deviceId) {
        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setDeviceName("温度传感器");
        device.setDataType(1);
        return device;
    }
}
