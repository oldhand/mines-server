package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.RestrictedAreaData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestrictedAreaDataRepository extends
        JpaRepository<RestrictedAreaData, Long>,
        JpaSpecificationExecutor<RestrictedAreaData> {
}
