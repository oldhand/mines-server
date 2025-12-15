package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.PointRealData;

public interface PointRealDataRepository extends JpaRepository<PointRealData, Long> {
}
