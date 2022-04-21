package com.example.urlshortener.repository;

import com.example.urlshortener.entity.CompanyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyTableRepository extends JpaRepository<CompanyTable, Integer> {
    CompanyTable findCompanyByCompanyName(String companyName);
}
