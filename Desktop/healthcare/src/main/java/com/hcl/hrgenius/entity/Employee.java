package com.hcl.hrgenius.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String employeeNumber;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String department;
  private String jobTitle;
  private String employmentStatus;
  private LocalDate hireDate;
  private BigDecimal annualSalary;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<PayrollRecord> payrollRecords = new ArrayList<>();

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<PerformanceReview> performanceReviews = new ArrayList<>();

  public Employee() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmployeeNumber() {
    return employeeNumber;
  }

  public void setEmployeeNumber(String employeeNumber) {
    this.employeeNumber = employeeNumber;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getEmploymentStatus() {
    return employmentStatus;
  }

  public void setEmploymentStatus(String employmentStatus) {
    this.employmentStatus = employmentStatus;
  }

  public LocalDate getHireDate() {
    return hireDate;
  }

  public void setHireDate(LocalDate hireDate) {
    this.hireDate = hireDate;
  }

  public BigDecimal getAnnualSalary() {
    return annualSalary;
  }

  public void setAnnualSalary(BigDecimal annualSalary) {
    this.annualSalary = annualSalary;
  }

  public List<PayrollRecord> getPayrollRecords() {
    return payrollRecords;
  }

  public void setPayrollRecords(List<PayrollRecord> payrollRecords) {
    this.payrollRecords = payrollRecords;
  }

  public List<PerformanceReview> getPerformanceReviews() {
    return performanceReviews;
  }

  public void setPerformanceReviews(List<PerformanceReview> performanceReviews) {
    this.performanceReviews = performanceReviews;
  }
}