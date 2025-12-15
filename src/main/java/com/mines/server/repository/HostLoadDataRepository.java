package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.HostLoadData;

public interface HostLoadDataRepository extends JpaRepository<HostLoadData, Long> {
}
