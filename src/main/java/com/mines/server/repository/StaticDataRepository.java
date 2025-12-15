package com.mines.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.mines.server.model.data.StaticData;
import java.util.Optional;

public interface StaticDataRepository extends JpaRepository<StaticData, Long> {
    boolean existsByEnterpriseCode(String enterpriseCode);

    // 新增：通过矿山编码查询静态数据
    Optional<StaticData> findByEnterpriseCode(String enterpriseCode);
}