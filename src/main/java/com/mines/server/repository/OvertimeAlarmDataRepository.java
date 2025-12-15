package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.OvertimeAlarmData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface OvertimeAlarmDataRepository extends
        JpaRepository<OvertimeAlarmData, Long>,
        JpaSpecificationExecutor<OvertimeAlarmData> {
}
