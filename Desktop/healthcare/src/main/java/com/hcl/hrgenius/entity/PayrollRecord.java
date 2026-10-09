package com.hcl.hrgenius.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payroll_record")
public class PayrollRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  private String payPeriod;
  private BigDecimal grossPay;
  private BigDecimal deductions;
  private BigDecimal netPay;
  private LocalDate processedDate;

  public PayrollRecord() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public String getPayPeriod() {
    return payPeriod;
  }

  public void setPayPeriod(String payPeriod) {
    this.payPeriod = payPeriod;
  }

  public BigDecimal getGrossPay() {
    return grossPay;
  }

  public void setGrossPay(BigDecimal grossPay) {
    this.grossPay = grossPay;
  }

  public BigDecimal getDeductions() {
    return deductions;
  }

  public void setDeductions(BigDecimal deductions) {
    this.deductions = deductions;
  }

  public BigDecimal getNetPay() {
    return netPay;
  }

  public void setNetPay(BigDecimal netPay) {
    this.netPay = netPay;
  }

  public LocalDate getProcessedDate() {
    return processedDate;
  }

  public void setProcessedDate(LocalDate processedDate) {
    this.processedDate = processedDate;
  }
}