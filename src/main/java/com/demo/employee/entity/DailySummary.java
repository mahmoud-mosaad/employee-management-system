package com.demo.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "DAILY_SUMMARY")
public class DailySummary extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
    private Department department;

    @Column(name = "SUMMARY_DATE")
    private LocalDate summaryDate;

    @Column(name = "TOTAL_EMPLOYEES")
    private Long totalEmployees;

}
