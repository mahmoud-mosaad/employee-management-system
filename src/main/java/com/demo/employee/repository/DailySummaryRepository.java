package com.demo.employee.repository;

import com.demo.employee.entity.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> { }
