package com.mines.server.util;

import com.mines.server.model.request.AppRequest;
import com.mines.server.model.request.RealTimeData;
import com.mines.server.model.data.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 数据验证工具（校验文档要求的必填字段和格式）
 */
public class ValidationUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** 验证外层请求字段 */
    public static void validateAppRequest(AppRequest request) {
        if (request.getAppId() == null || request.getAppId().isEmpty()) {
            throw new IllegalArgumentException("appId不能为空");
        }
        if (request.getServiceId() == null || request.getServiceId().isEmpty()) {
            throw new IllegalArgumentException("serviceId不能为空");
        }
        if (request.getDataId() == null || request.getDataId().isEmpty()) {
            throw new IllegalArgumentException("dataId不能为空");
        }
        if (request.getData() == null || request.getData().isEmpty()) {
            throw new IllegalArgumentException("data不能为空");
        }
    }

    /** 验证实时数据主体字段 */
    public static void validateRealTimeData(RealTimeData data) {
        if (data.getDataId() == null || data.getDataId().isEmpty()) {
            throw new IllegalArgumentException("RealTimeData.dataId不能为空");
        }
        if (data.getEnterpriseCode() == null || data.getEnterpriseCode().isEmpty()) {
            throw new IllegalArgumentException("enterpriseCode不能为空");
        }
        if (!"DXK".equals(data.getEnterpriseType())) {
            throw new IllegalArgumentException("enterpriseType必须为DXK");
        }
        if (data.getGatewayCode() == null || data.getGatewayCode().isEmpty()) {
            throw new IllegalArgumentException("gatewayCode不能为空");
        }
        // 验证时间格式
        try {
            DATE_FORMAT.parse(data.getCollectTime());
        } catch (Exception e) {
            throw new IllegalArgumentException("collectTime格式错误，需为yyyy-MM-dd HH:mm:ss");
        }
        if (data.getIsConnectDataSource() == null) {
            throw new IllegalArgumentException("isConnectDataSource不能为空");
        }
        if (!"report".equals(data.getReportType()) && !"continues".equals(data.getReportType())) {
            throw new IllegalArgumentException("reportType必须为report或continues");
        }
        if (data.getDatas() == null || data.getDatas().isEmpty()) {
            throw new IllegalArgumentException("datas不能为空");
        }
    }

    // 以下为12种数据类型的字段验证方法（示例展示部分，其余类似）
    public static void validatePowerData(List<PowerData> datas) {
        for (PowerData data : datas) {
            if (data.getKsbh() == null || data.getKsbh().isEmpty()) {
                throw new IllegalArgumentException("PowerData.ksbh不能为空");
            }
            if (data.getPowerCode() == null || data.getPowerCode().isEmpty()) {
                throw new IllegalArgumentException("PowerData.powerCode不能为空");
            }
            if (data.getPowerDate() == null) {
                throw new IllegalArgumentException("PowerData.powerDate不能为空");
            }
            if (data.getPowerConsumption() == null) {
                throw new IllegalArgumentException("PowerData.powerConsumption不能为空");
            }
            if (data.getDataTime() == null) {
                throw new IllegalArgumentException("PowerData.dataTime不能为空");
            }
        }
    }

    public static void validateHostLoadData(List<HostLoadData> datas) {
        for (HostLoadData data : datas) {
            // 验证逻辑类似，检查必填字段
            if (data.getKsbh() == null || data.getKsbh().isEmpty()) {
                throw new IllegalArgumentException("HostLoadData.ksbh不能为空");
            }
            // ... 其他字段验证
        }
    }

    // 其余数据类型的validate方法（略，逻辑与上述一致）
    public static void validateHostLoadAlarmData(List<HostLoadAlarmData> datas) { /* 实现 */ }
    public static void validatePointRealData(List<PointRealData> datas) { /* 实现 */ }
    public static void validatePointStatData(List<PointStatData> datas) { /* 实现 */ }
    public static void validatePointAbnormalData(List<PointAbnormalData> datas) { /* 实现 */ }
    public static void validatePersonLocationData(List<PersonLocationData> datas) { /* 实现 */ }
    public static void validateOvertimeAlarmData(List<OvertimeAlarmData> datas) { /* 实现 */ }
    public static void validateOvercrowdAlarmData(List<OvercrowdAlarmData> datas) { /* 实现 */ }
    public static void validateRestrictedAreaData(List<RestrictedAreaData> datas) { /* 实现 */ }
    public static void validatePersonHelpData(List<PersonHelpData> datas) { /* 实现 */ }
    public static void validateSubstationRunData(List<SubstationRunData> datas) { /* 实现 */ }
}
