package com.example.urlshortener.repository;

import com.example.urlshortener.entity.SpecialShortTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpecialShortTableRepository extends JpaRepository<SpecialShortTable, Integer> {
    @Query(value="SELECT COUNT(*) FROM `special-short-table` WHERE `company-id`=?1", nativeQuery = true)
    Integer findCountOfCompany(Integer companyId);
}
