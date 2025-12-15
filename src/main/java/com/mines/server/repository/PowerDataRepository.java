package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.PowerData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PowerDataRepository extends
        JpaRepository<PowerData, Long>,
        JpaSpecificationExecutor<PowerData> {
}
