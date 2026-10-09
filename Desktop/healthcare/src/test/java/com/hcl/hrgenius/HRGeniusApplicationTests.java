package com.hcl.hrgenius;

import com.hcl.hrgenius.entity.Employee;
import com.hcl.hrgenius.exception.BadRequestException;
import com.hcl.hrgenius.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class HRGeniusApplicationTests {

  @Autowired
  private EmployeeService employeeService;

  @BeforeEach
  void clearData() {
    employeeService.getEmployees().forEach(employee -> employeeService.deleteEmployee(employee.getId()));
  }

  @Test
  void contextLoads() {
  }

  @Test
  void employeeCrudPersistsAndSearchesByDepartment() {
    Employee employee = new Employee();
    employee.setEmployeeNumber("EMP-TEST");
    employee.setFirstName("Test");
    employee.setLastName("Employee");
    employee.setEmail("test.employee@example.com");
    employee.setDepartment("Engineering");
    employee.setAnnualSalary(BigDecimal.valueOf(100000));

    Employee saved = employeeService.saveEmployee(employee);

    assertEquals(1, employeeService.searchEmployees(null, "engineering").size());
    assertEquals(saved.getId(), employeeService.getEmployee(saved.getId()).orElseThrow().getId());
  }

  @Test
  void invalidEmployeeEmailIsRejected() {
    Employee employee = new Employee();
    employee.setFirstName("Test");
    employee.setLastName("Employee");
    employee.setEmail("not-an-email");

    assertThrows(BadRequestException.class, () -> employeeService.saveEmployee(employee));
  }
}