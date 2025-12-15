package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.OvercrowdAlarmData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OvercrowdAlarmDataRepository extends
        JpaRepository<OvercrowdAlarmData, Long>,
        JpaSpecificationExecutor<OvercrowdAlarmData> {
}
