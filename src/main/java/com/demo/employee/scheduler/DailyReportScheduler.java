package com.demo.employee.scheduler;

import com.demo.employee.entity.DailySummary;
import com.demo.employee.repository.DepartmentRepository;
import com.demo.employee.repository.DailySummaryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DailyReportScheduler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final DepartmentRepository departmentRepository;
    private final DailySummaryRepository dailySummaryRepository;

    @Transactional
    @Scheduled(cron = "0 0 9 * * *", zone = "Africa/Cairo") // Every day 9 AM
    public void generateReport() {
        departmentRepository.findAll().forEach(dept -> {
            long count = dept.getEmployees() == null ? 0 : dept.getEmployees().size();

            log.info("Daily Summary: {} = {}", dept.getName(), count);

            DailySummary ds = new DailySummary();
            ds.setSummaryDate(LocalDate.now());
            ds.setDepartment(dept);
            ds.setTotalEmployees(count);
            dailySummaryRepository.save(ds);
        });
    }
}
