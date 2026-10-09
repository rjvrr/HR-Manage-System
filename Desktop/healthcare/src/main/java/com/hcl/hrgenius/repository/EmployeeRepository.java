package com.hcl.hrgenius.repository;

import com.hcl.hrgenius.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findByDepartmentIgnoreCase(String department);

  @Query("SELECT e FROM Employee e WHERE " +
         "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
         "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
         "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
         "LOWER(e.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
         "LOWER(e.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
         "LOWER(e.employeeNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<Employee> searchEmployees(@Param("keyword") String keyword);
}