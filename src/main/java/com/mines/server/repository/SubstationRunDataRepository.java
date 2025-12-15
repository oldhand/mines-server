package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.SubstationRunData;

public interface SubstationRunDataRepository extends JpaRepository<SubstationRunData, Long> {
}
