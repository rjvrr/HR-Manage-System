package com.hcl.hrgenius.controller;

import com.hcl.hrgenius.entity.*;
import com.hcl.hrgenius.exception.ResourceNotFoundException;
import com.hcl.hrgenius.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://127.0.0.1:4200", "http://127.0.0.1:4201" })
@RequestMapping("/api")
public class HRGeniusController {
  private final EmployeeService employeeService;

  public HRGeniusController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping("/health")
  public ResponseEntity<Map<String, String>> healthCheck() {
    return ResponseEntity.ok(Map.of("status", "UP", "service", "HRGenius", "domain", "HR"));
  }

  @GetMapping("/analytics")
  public Map<String, Object> getAnalytics() {
    return employeeService.getAnalytics();
  }

  // ===================== EMPLOYEES =====================

  @GetMapping("/employees")
  public List<Employee> getEmployees(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String department) {
    if ((search != null && !search.isBlank()) || (department != null && !department.isBlank())) {
      return employeeService.searchEmployees(search, department);
    }
    return employeeService.getEmployees();
  }

  @GetMapping("/employees/{id}")
  public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
    return employeeService.getEmployee(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
  }

  @PostMapping("/employees")
  public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.saveEmployee(employee));
  }

  @PutMapping("/employees/{id}")
  public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
    return employeeService.updateEmployee(id, employee)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
  }

  @DeleteMapping("/employees/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    if (!employeeService.deleteEmployee(id)) {
      throw new ResourceNotFoundException("Employee not found with id: " + id);
    }
    return ResponseEntity.noContent().build();
  }

  // ===================== CANDIDATES / RECRUITMENT =====================

  @GetMapping("/candidates")
  public List<Candidate> getCandidates() {
    return employeeService.getCandidates();
  }

  @GetMapping("/candidates/{id}")
  public ResponseEntity<Candidate> getCandidate(@PathVariable Long id) {
    return employeeService.getCandidate(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
  }

  @PostMapping("/candidates")
  public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate candidate) {
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.saveCandidate(candidate));
  }

  @PutMapping("/candidates/{id}")
  public ResponseEntity<Candidate> updateCandidate(@PathVariable Long id, @RequestBody Candidate candidate) {
    return employeeService.updateCandidate(id, candidate)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
  }

  @DeleteMapping("/candidates/{id}")
  public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
    if (!employeeService.deleteCandidate(id)) {
      throw new ResourceNotFoundException("Candidate not found with id: " + id);
    }
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/candidates/{id}/onboard")
  public ResponseEntity<Employee> onboardCandidate(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.onboardCandidate(id));
  }

  // ===================== JOBS =====================

  @GetMapping("/jobs")
  public List<Job> getJobs() {
    return employeeService.getJobs();
  }

  @GetMapping("/jobs/{id}")
  public ResponseEntity<Job> getJob(@PathVariable Long id) {
    return employeeService.getJob(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
  }

  @PostMapping("/jobs")
  public ResponseEntity<Job> createJob(@RequestBody Job job) {
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.saveJob(job));
  }

  @PutMapping("/jobs/{id}")
  public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
    return employeeService.updateJob(id, job)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
  }

  @DeleteMapping("/jobs/{id}")
  public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
    if (!employeeService.deleteJob(id)) {
      throw new ResourceNotFoundException("Job not found with id: " + id);
    }
    return ResponseEntity.noContent().build();
  }

  // ===================== PAYROLL =====================

  @GetMapping("/payroll")
  public List<PayrollRecord> getPayroll() {
    return employeeService.getPayroll();
  }

  @GetMapping("/payroll/{id}")
  public ResponseEntity<PayrollRecord> getPayrollRecord(@PathVariable Long id) {
    return employeeService.getPayrollRecord(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found with id: " + id));
  }

  @PostMapping("/payroll")
  public ResponseEntity<PayrollRecord> createPayroll(@RequestBody PayrollRecord payroll) {
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.savePayroll(payroll));
  }

  @PutMapping("/payroll/{id}")
  public ResponseEntity<PayrollRecord> updatePayroll(@PathVariable Long id, @RequestBody PayrollRecord payroll) {
    return employeeService.updatePayroll(id, payroll)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found with id: " + id));
  }

  @DeleteMapping("/payroll/{id}")
  public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
    if (!employeeService.deletePayroll(id)) {
      throw new ResourceNotFoundException("Payroll record not found with id: " + id);
    }
    return ResponseEntity.noContent().build();
  }

  // ===================== PERFORMANCE REVIEWS =====================

  @GetMapping("/performance-reviews")
  public List<PerformanceReview> getPerformanceReviews() {
    return employeeService.getPerformanceReviews();
  }

  @GetMapping("/performance-reviews/{id}")
  public ResponseEntity<PerformanceReview> getPerformanceReview(@PathVariable Long id) {
    return employeeService.getPerformanceReview(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + id));
  }

  @PostMapping("/performance-reviews")
  public ResponseEntity<PerformanceReview> createPerformanceReview(@RequestBody PerformanceReview review) {
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.savePerformanceReview(review));
  }

  @PutMapping("/performance-reviews/{id}")
  public ResponseEntity<PerformanceReview> updatePerformanceReview(@PathVariable Long id, @RequestBody PerformanceReview review) {
    return employeeService.updatePerformanceReview(id, review)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + id));
  }

  @DeleteMapping("/performance-reviews/{id}")
  public ResponseEntity<Void> deletePerformanceReview(@PathVariable Long id) {
    if (!employeeService.deletePerformanceReview(id)) {
      throw new ResourceNotFoundException("Performance review not found with id: " + id);
    }
    return ResponseEntity.noContent().build();
  }
}