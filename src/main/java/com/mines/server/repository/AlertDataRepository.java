package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.mines.server.model.data.AlertData;

public interface AlertDataRepository extends
        JpaRepository<AlertData, Long>,
        JpaSpecificationExecutor<AlertData> {
}