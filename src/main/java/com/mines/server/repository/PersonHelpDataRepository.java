package com.mines.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.PersonHelpData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // 新增导入
import com.mines.server.model.data.PersonHelpData;

public interface PersonHelpDataRepository extends
        JpaRepository<PersonHelpData, Long>,
        JpaSpecificationExecutor<PersonHelpData> {
}

