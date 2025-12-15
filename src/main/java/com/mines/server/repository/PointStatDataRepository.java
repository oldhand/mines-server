package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.PointStatData;

public interface PointStatDataRepository extends JpaRepository<PointStatData, Long> {
}
