import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Employee {
  id?: number;
  employeeNumber: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  department: string;
  jobTitle: string;
  employmentStatus: string;
  hireDate: string;
  annualSalary: number;
}

export interface Candidate {
  id?: number;
  name: string;
  email: string;
  phone: string;
  jobTitle: string;
  status: string;
  appliedDate: string;
}

export interface Job {
  id?: number;
  title: string;
  department: string;
  employmentType: string;
  status: string;
  description: string;
}

export interface PayrollRecord {
  id?: number;
  employee: Employee;
  payPeriod: string;
  grossPay: number;
  deductions: number;
  netPay: number;
  processedDate: string;
}

export interface PerformanceReview {
  id?: number;
  employee: Employee;
  reviewPeriod: string;
  rating: number;
  comments: string;
  reviewDate: string;
}

export interface PayrollPayload {
  id?: number;
  employee: { id: number };
  payPeriod: string;
  grossPay: number;
  deductions: number;
  netPay: number;
  processedDate: string;
}

export interface PerformanceReviewPayload {
  id?: number;
  employee: { id: number };
  reviewPeriod: string;
  rating: number;
  comments: string;
  reviewDate: string;
}

export interface Analytics {
  totalEmployees: number;
  totalCandidates: number;
  totalPayrollRecords: number;
  totalJobs?: number;
  averageRating?: number;
  employeesByDepartment: Record<string, number>;
}

@Injectable({ providedIn: 'root' })
export class HrApiService {
  private readonly apiUrl = 'http://localhost:8080/api';

  constructor(private readonly http: HttpClient) {}

  // Health
  getHealth(): Observable<{ status: string; service: string; domain: string }> {
    return this.http.get<{ status: string; service: string; domain: string }>(`${this.apiUrl}/health`);
  }

  // Analytics
  getAnalytics(): Observable<Analytics> {
    return this.http.get<Analytics>(`${this.apiUrl}/analytics`);
  }

  // Employees
  getEmployees(search?: string, department?: string): Observable<Employee[]> {
    let params = new HttpParams();
    if (search && search.trim()) {
      params = params.set('search', search.trim());
    }
    if (department && department.trim() && department !== 'All departments') {
      params = params.set('department', department.trim());
    }
    return this.http.get<Employee[]>(`${this.apiUrl}/employees`, { params });
  }

  getEmployee(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/employees/${id}`);
  }

  createEmployee(employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(`${this.apiUrl}/employees`, employee);
  }

  updateEmployee(id: number, employee: Employee): Observable<Employee> {
    return this.http.put<Employee>(`${this.apiUrl}/employees/${id}`, employee);
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/employees/${id}`);
  }

  // Candidates / Recruitment
  getCandidates(): Observable<Candidate[]> {
    return this.http.get<Candidate[]>(`${this.apiUrl}/candidates`);
  }

  getCandidate(id: number): Observable<Candidate> {
    return this.http.get<Candidate>(`${this.apiUrl}/candidates/${id}`);
  }

  createCandidate(candidate: Candidate): Observable<Candidate> {
    return this.http.post<Candidate>(`${this.apiUrl}/candidates`, candidate);
  }

  updateCandidate(id: number, candidate: Candidate): Observable<Candidate> {
    return this.http.put<Candidate>(`${this.apiUrl}/candidates/${id}`, candidate);
  }

  deleteCandidate(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/candidates/${id}`);
  }

  onboardCandidate(id: number): Observable<Employee> {
    return this.http.post<Employee>(`${this.apiUrl}/candidates/${id}/onboard`, {});
  }

  // Jobs
  getJobs(): Observable<Job[]> {
    return this.http.get<Job[]>(`${this.apiUrl}/jobs`);
  }

  getJob(id: number): Observable<Job> {
    return this.http.get<Job>(`${this.apiUrl}/jobs/${id}`);
  }

  createJob(job: Job): Observable<Job> {
    return this.http.post<Job>(`${this.apiUrl}/jobs`, job);
  }

  updateJob(id: number, job: Job): Observable<Job> {
    return this.http.put<Job>(`${this.apiUrl}/jobs/${id}`, job);
  }

  deleteJob(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/jobs/${id}`);
  }

  // Payroll
  getPayroll(): Observable<PayrollRecord[]> {
    return this.http.get<PayrollRecord[]>(`${this.apiUrl}/payroll`);
  }

  getPayrollRecord(id: number): Observable<PayrollRecord> {
    return this.http.get<PayrollRecord>(`${this.apiUrl}/payroll/${id}`);
  }

  createPayroll(payroll: PayrollPayload): Observable<PayrollRecord> {
    return this.http.post<PayrollRecord>(`${this.apiUrl}/payroll`, payroll);
  }

  updatePayroll(id: number, payroll: PayrollPayload): Observable<PayrollRecord> {
    return this.http.put<PayrollRecord>(`${this.apiUrl}/payroll/${id}`, payroll);
  }

  deletePayroll(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/payroll/${id}`);
  }

  // Performance Reviews
  getPerformanceReviews(): Observable<PerformanceReview[]> {
    return this.http.get<PerformanceReview[]>(`${this.apiUrl}/performance-reviews`);
  }

  getPerformanceReview(id: number): Observable<PerformanceReview> {
    return this.http.get<PerformanceReview>(`${this.apiUrl}/performance-reviews/${id}`);
  }

  createPerformanceReview(review: PerformanceReviewPayload): Observable<PerformanceReview> {
    return this.http.post<PerformanceReview>(`${this.apiUrl}/performance-reviews`, review);
  }

  updatePerformanceReview(id: number, review: PerformanceReviewPayload): Observable<PerformanceReview> {
    return this.http.put<PerformanceReview>(`${this.apiUrl}/performance-reviews/${id}`, review);
  }

  deletePerformanceReview(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/performance-reviews/${id}`);
  }
}
