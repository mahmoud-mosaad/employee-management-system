package com.demo.employee.scheduler;

import com.demo.employee.entity.DailySummary;
import com.demo.employee.entity.Department;
import com.demo.employee.entity.Employee;
import com.demo.employee.repository.DailySummaryRepository;
import com.demo.employee.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyReportSchedulerTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DailySummaryRepository summaryRepo;

    @InjectMocks
    private DailyReportScheduler dailyReportScheduler;

    @Test
    void testGenerateReport_Success() {
        Department hrDept = new Department();
        hrDept.setName("HR");
        hrDept.setEmployees(List.of(new Employee(), new Employee()));

        Department itDept = new Department();
        itDept.setName("IT");
        itDept.setEmployees(Collections.emptyList());

        List<Department> mockDepartments = List.of(hrDept, itDept);
        when(departmentRepository.findAll()).thenReturn(mockDepartments);

        dailyReportScheduler.generateReport();

        verify(summaryRepo, times(2)).save(org.mockito.ArgumentMatchers.any(DailySummary.class));

        ArgumentCaptor<DailySummary> summaryCaptor = ArgumentCaptor.forClass(DailySummary.class);
        verify(summaryRepo, times(2)).save(summaryCaptor.capture());
        List<DailySummary> capturedSummaries = summaryCaptor.getAllValues();

        DailySummary hrSummary = capturedSummaries.stream()
                .filter(s -> s.getDepartment().getName().equals("HR"))
                .findFirst().orElseThrow();
        assertEquals(LocalDate.now(), hrSummary.getSummaryDate());
        assertEquals(2L, hrSummary.getTotalEmployees());

        DailySummary itSummary = capturedSummaries.stream()
                .filter(s -> s.getDepartment().getName().equals("IT"))
                .findFirst().orElseThrow();
        assertEquals(LocalDate.now(), itSummary.getSummaryDate());
        assertEquals(0L, itSummary.getTotalEmployees());
    }
}
