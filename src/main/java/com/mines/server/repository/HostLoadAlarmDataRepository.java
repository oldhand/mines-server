package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.HostLoadAlarmData;

public interface HostLoadAlarmDataRepository extends JpaRepository<HostLoadAlarmData, Long> {
}
