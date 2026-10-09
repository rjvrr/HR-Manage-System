package com.hcl.hrgenius.service;

import com.hcl.hrgenius.entity.*;
import com.hcl.hrgenius.exception.BadRequestException;
import com.hcl.hrgenius.exception.ResourceNotFoundException;
import com.hcl.hrgenius.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class EmployeeService {
  private final EmployeeRepository employeeRepository;
  private final CandidateRepository candidateRepository;
  private final PayrollRepository payrollRepository;
  private final PerformanceReviewRepository performanceReviewRepository;
  private final JobRepository jobRepository;

  public EmployeeService(EmployeeRepository employeeRepository, CandidateRepository candidateRepository,
      PayrollRepository payrollRepository, PerformanceReviewRepository performanceReviewRepository,
      JobRepository jobRepository) {
    this.employeeRepository = employeeRepository;
    this.candidateRepository = candidateRepository;
    this.payrollRepository = payrollRepository;
    this.performanceReviewRepository = performanceReviewRepository;
    this.jobRepository = jobRepository;
  }

  // ===================== EMPLOYEE MODULE =====================

  public List<Employee> getEmployees() {
    return employeeRepository.findAll();
  }

  public List<Employee> searchEmployees(String search, String department) {
    List<Employee> list;
    if (search != null && !search.trim().isEmpty()) {
      list = employeeRepository.searchEmployees(search.trim());
    } else if (department != null && !department.trim().isEmpty()
        && !"All departments".equalsIgnoreCase(department.trim())) {
      list = employeeRepository.findByDepartmentIgnoreCase(department.trim());
    } else {
      list = employeeRepository.findAll();
    }

    if (department != null && !department.trim().isEmpty() && !"All departments".equalsIgnoreCase(department.trim())) {
      list = list.stream()
          .filter(e -> department.equalsIgnoreCase(e.getDepartment()))
          .toList();
    }
    return list;
  }

  public Optional<Employee> getEmployee(Long id) {
    return employeeRepository.findById(id);
  }

  @Transactional
  public Employee saveEmployee(Employee employee) {
    validateEmployee(employee);
    if (employee.getEmploymentStatus() == null || employee.getEmploymentStatus().trim().isEmpty()) {
      employee.setEmploymentStatus("Active");
    }
    if (employee.getHireDate() == null) {
      employee.setHireDate(LocalDate.now());
    }
    if (employee.getAnnualSalary() == null) {
      employee.setAnnualSalary(BigDecimal.ZERO);
    }
    return employeeRepository.save(employee);
  }

  @Transactional
  public Optional<Employee> updateEmployee(Long id, Employee input) {
    validateEmployee(input);
    return employeeRepository.findById(id).map(existing -> {
      existing.setEmployeeNumber(input.getEmployeeNumber());
      existing.setFirstName(input.getFirstName());
      existing.setLastName(input.getLastName());
      existing.setEmail(input.getEmail());
      existing.setPhone(input.getPhone());
      existing.setDepartment(input.getDepartment());
      existing.setJobTitle(input.getJobTitle());
      existing.setEmploymentStatus(
          input.getEmploymentStatus() != null ? input.getEmploymentStatus() : existing.getEmploymentStatus());
      if (input.getHireDate() != null) {
        existing.setHireDate(input.getHireDate());
      }
      if (input.getAnnualSalary() != null) {
        existing.setAnnualSalary(input.getAnnualSalary());
      }
      return employeeRepository.save(existing);
    });
  }

  @Transactional
  public boolean deleteEmployee(Long id) {
    if (!employeeRepository.existsById(id)) {
      return false;
    }
    // Clean up associated payroll records and performance reviews
    List<PayrollRecord> payrolls = payrollRepository.findByEmployeeId(id);
    if (!payrolls.isEmpty()) {
      payrollRepository.deleteAll(payrolls);
    }
    List<PerformanceReview> reviews = performanceReviewRepository.findByEmployeeId(id);
    if (!reviews.isEmpty()) {
      performanceReviewRepository.deleteAll(reviews);
    }
    employeeRepository.deleteById(id);
    return true;
  }

  // ===================== RECRUITMENT / CANDIDATE MODULE =====================

  public List<Candidate> getCandidates() {
    return candidateRepository.findAll();
  }

  public Optional<Candidate> getCandidate(Long id) {
    return candidateRepository.findById(id);
  }

  @Transactional
  public Candidate saveCandidate(Candidate candidate) {
    if (candidate.getName() == null || candidate.getName().trim().isEmpty()) {
      throw new BadRequestException("Candidate name is required");
    }
    if (candidate.getEmail() == null || candidate.getEmail().trim().isEmpty()) {
      throw new BadRequestException("Candidate email is required");
    }
    if (candidate.getStatus() == null || candidate.getStatus().trim().isEmpty()) {
      candidate.setStatus("Applied");
    }
    if (candidate.getAppliedDate() == null) {
      candidate.setAppliedDate(LocalDate.now());
    }
    return candidateRepository.save(candidate);
  }

  @Transactional
  public Optional<Candidate> updateCandidate(Long id, Candidate input) {
    return candidateRepository.findById(id).map(existing -> {
      existing.setName(input.getName());
      existing.setEmail(input.getEmail());
      existing.setPhone(input.getPhone());
      existing.setJobTitle(input.getJobTitle());
      if (input.getStatus() != null && !input.getStatus().trim().isEmpty()) {
        existing.setStatus(input.getStatus());
      }
      if (input.getAppliedDate() != null) {
        existing.setAppliedDate(input.getAppliedDate());
      }
      return candidateRepository.save(existing);
    });
  }

  @Transactional
  public boolean deleteCandidate(Long id) {
    if (!candidateRepository.existsById(id)) {
      return false;
    }
    candidateRepository.deleteById(id);
    return true;
  }

  @Transactional
  public Employee onboardCandidate(Long id) {
    Candidate candidate = candidateRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

    candidate.setStatus("Hired");
    candidateRepository.save(candidate);

    // Split name into first and last
    String[] parts = candidate.getName().trim().split("\\s+", 2);
    String firstName = parts[0];
    String lastName = parts.length > 1 ? parts[1] : "";

    Employee employee = new Employee();
    employee.setFirstName(firstName);
    employee.setLastName(lastName);
    employee.setEmail(candidate.getEmail());
    employee.setPhone(candidate.getPhone());
    employee.setJobTitle(candidate.getJobTitle());
    employee.setDepartment("General");
    employee.setEmploymentStatus("Onboarding");
    employee.setEmployeeNumber("EMP" + System.currentTimeMillis() % 10000);
    employee.setHireDate(LocalDate.now());
    employee.setAnnualSalary(BigDecimal.ZERO);

    return employeeRepository.save(employee);
  }

  // ===================== JOB REQUISITION MODULE =====================

  public List<Job> getJobs() {
    return jobRepository.findAll();
  }

  public Optional<Job> getJob(Long id) {
    return jobRepository.findById(id);
  }

  @Transactional
  public Job saveJob(Job job) {
    if (job.getTitle() == null || job.getTitle().trim().isEmpty()) {
      throw new BadRequestException("Job title is required");
    }
    if (job.getStatus() == null || job.getStatus().trim().isEmpty()) {
      job.setStatus("Open");
    }
    if (job.getEmploymentType() == null || job.getEmploymentType().trim().isEmpty()) {
      job.setEmploymentType("Full-Time");
    }
    return jobRepository.save(job);
  }

  @Transactional
  public Optional<Job> updateJob(Long id, Job input) {
    return jobRepository.findById(id).map(existing -> {
      existing.setTitle(input.getTitle());
      existing.setDepartment(input.getDepartment());
      existing.setEmploymentType(input.getEmploymentType());
      existing.setStatus(input.getStatus());
      existing.setDescription(input.getDescription());
      return jobRepository.save(existing);
    });
  }

  @Transactional
  public boolean deleteJob(Long id) {
    if (!jobRepository.existsById(id)) {
      return false;
    }
    jobRepository.deleteById(id);
    return true;
  }

  // ===================== PAYROLL MODULE =====================

  public List<PayrollRecord> getPayroll() {
    return payrollRepository.findAll();
  }

  public Optional<PayrollRecord> getPayrollRecord(Long id) {
    return payrollRepository.findById(id);
  }

  @Transactional
  public PayrollRecord savePayroll(PayrollRecord payroll) {
    if (payroll.getEmployee() == null || payroll.getEmployee().getId() == null) {
      throw new BadRequestException("Valid employee is required for payroll record");
    }
    Employee employee = employeeRepository.findById(payroll.getEmployee().getId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Employee not found with id: " + payroll.getEmployee().getId()));
    payroll.setEmployee(employee);
    validatePayrollAmounts(payroll);

    if (payroll.getGrossPay() == null) {
      payroll.setGrossPay(BigDecimal.ZERO);
    }
    if (payroll.getDeductions() == null) {
      payroll.setDeductions(BigDecimal.ZERO);
    }
    if (payroll.getNetPay() == null || payroll.getNetPay().compareTo(BigDecimal.ZERO) == 0) {
      payroll.setNetPay(payroll.getGrossPay().subtract(payroll.getDeductions()));
    }
    if (payroll.getProcessedDate() == null) {
      payroll.setProcessedDate(LocalDate.now());
    }
    return payrollRepository.save(payroll);
  }

  @Transactional
  public Optional<PayrollRecord> updatePayroll(Long id, PayrollRecord input) {
    validatePayrollAmounts(input);
    return payrollRepository.findById(id).map(existing -> {
      if (input.getEmployee() != null && input.getEmployee().getId() != null) {
        Employee employee = employeeRepository.findById(input.getEmployee().getId())
            .orElseThrow(
                () -> new ResourceNotFoundException("Employee not found with id: " + input.getEmployee().getId()));
        existing.setEmployee(employee);
      }
      existing.setPayPeriod(input.getPayPeriod());
      if (input.getGrossPay() != null) {
        existing.setGrossPay(input.getGrossPay());
      }
      if (input.getDeductions() != null) {
        existing.setDeductions(input.getDeductions());
      }
      if (input.getNetPay() != null) {
        existing.setNetPay(input.getNetPay());
      } else {
        existing.setNetPay(existing.getGrossPay().subtract(existing.getDeductions()));
      }
      if (input.getProcessedDate() != null) {
        existing.setProcessedDate(input.getProcessedDate());
      }
      return payrollRepository.save(existing);
    });
  }

  @Transactional
  public boolean deletePayroll(Long id) {
    if (!payrollRepository.existsById(id)) {
      return false;
    }
    payrollRepository.deleteById(id);
    return true;
  }

  // ===================== PERFORMANCE MODULE =====================

  public List<PerformanceReview> getPerformanceReviews() {
    return performanceReviewRepository.findAll();
  }

  public Optional<PerformanceReview> getPerformanceReview(Long id) {
    return performanceReviewRepository.findById(id);
  }

  @Transactional
  public PerformanceReview savePerformanceReview(PerformanceReview review) {
    if (review.getEmployee() == null || review.getEmployee().getId() == null) {
      throw new BadRequestException("Valid employee is required for performance review");
    }
    Employee employee = employeeRepository.findById(review.getEmployee().getId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Employee not found with id: " + review.getEmployee().getId()));
    review.setEmployee(employee);
    validateRating(review.getRating());

    if (review.getReviewDate() == null) {
      review.setReviewDate(LocalDate.now());
    }
    if (review.getRating() == null) {
      review.setRating(3);
    }
    return performanceReviewRepository.save(review);
  }

  @Transactional
  public Optional<PerformanceReview> updatePerformanceReview(Long id, PerformanceReview input) {
    validateRating(input.getRating());
    return performanceReviewRepository.findById(id).map(existing -> {
      if (input.getEmployee() != null && input.getEmployee().getId() != null) {
        Employee employee = employeeRepository.findById(input.getEmployee().getId())
            .orElseThrow(
                () -> new ResourceNotFoundException("Employee not found with id: " + input.getEmployee().getId()));
        existing.setEmployee(employee);
      }
      existing.setReviewPeriod(input.getReviewPeriod());
      if (input.getRating() != null) {
        existing.setRating(input.getRating());
      }
      existing.setComments(input.getComments());
      if (input.getReviewDate() != null) {
        existing.setReviewDate(input.getReviewDate());
      }
      return performanceReviewRepository.save(existing);
    });
  }

  @Transactional
  public boolean deletePerformanceReview(Long id) {
    if (!performanceReviewRepository.existsById(id)) {
      return false;
    }
    performanceReviewRepository.deleteById(id);
    return true;
  }

  // ===================== ANALYTICS MODULE =====================

  public Map<String, Object> getAnalytics() {
    List<Employee> employees = employeeRepository.findAll();
    Map<String, Long> byDepartment = new TreeMap<>();
    employees.forEach(employee -> byDepartment.merge(
        Optional.ofNullable(employee.getDepartment()).filter(department -> !department.isBlank()).orElse("Unassigned"),
        1L, Long::sum));

    List<PerformanceReview> reviews = performanceReviewRepository.findAll();
    double avgRating = reviews.stream()
        .filter(r -> r.getRating() != null)
        .mapToInt(PerformanceReview::getRating)
        .average()
        .orElse(0.0);

    return Map.of(
        "totalEmployees", employees.size(),
        "totalCandidates", candidateRepository.count(),
        "totalPayrollRecords", payrollRepository.count(),
        "totalJobs", jobRepository.count(),
        "employeesByDepartment", byDepartment,
        "averageRating", Math.round(avgRating * 10.0) / 10.0);
  }

  private void validateEmployee(Employee employee) {
    if (employee == null) {
      throw new BadRequestException("Employee payload is required");
    }
    if (isBlank(employee.getFirstName())) {
      throw new BadRequestException("First name is required");
    }
    if (isBlank(employee.getLastName())) {
      throw new BadRequestException("Last name is required");
    }
    if (isBlank(employee.getEmail()) || !employee.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      throw new BadRequestException("A valid email is required");
    }
    if (employee.getAnnualSalary() != null && employee.getAnnualSalary().signum() < 0) {
      throw new BadRequestException("Annual salary cannot be negative");
    }
  }

  private void validatePayrollAmounts(PayrollRecord payroll) {
    if (payroll == null) {
      throw new BadRequestException("Payroll payload is required");
    }
    if (payroll.getGrossPay() != null && payroll.getGrossPay().signum() < 0) {
      throw new BadRequestException("Gross pay cannot be negative");
    }
    if (payroll.getDeductions() != null && payroll.getDeductions().signum() < 0) {
      throw new BadRequestException("Deductions cannot be negative");
    }
  }

  private void validateRating(Integer rating) {
    if (rating != null && (rating < 1 || rating > 5)) {
      throw new BadRequestException("Rating must be between 1 and 5");
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}