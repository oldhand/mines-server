package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.PersonLocationData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonLocationDataRepository extends
        JpaRepository<PersonLocationData, Long>,
        JpaSpecificationExecutor<PersonLocationData> {
}
