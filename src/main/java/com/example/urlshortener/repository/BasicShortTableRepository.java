package com.example.urlshortener.repository;

import com.example.urlshortener.entity.BasicShortTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.sql.Timestamp;

public interface BasicShortTableRepository extends JpaRepository<BasicShortTable, Integer> {
    @Query(value = "select max(`pk-id`) from `basic-short-table`", nativeQuery = true)
    Integer getMaxValue();

    @Query(value = "select `full-url` from `basic-short-table` where `short-url`=?1 and `expires-at`>=?2", nativeQuery = true)
    String getFullUrl(String shortUrl, Timestamp currDate);
}
