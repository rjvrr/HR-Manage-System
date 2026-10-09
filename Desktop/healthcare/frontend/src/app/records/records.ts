import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';
import { Candidate, Employee, HrApiService, Job, PayrollPayload, PayrollRecord, PerformanceReview, PerformanceReviewPayload } from '../services/hr-api';

@Component({
  selector: 'app-records',
  imports: [CommonModule, FormsModule],
  templateUrl: './records.html',
  styleUrl: './records.css',
})
export class Records implements OnInit {
  resource = '';
  title = '';
  description = '';
  rows: (Candidate | Job | PayrollRecord | PerformanceReview)[] = [];
  filteredRows: (Candidate | Job | PayrollRecord | PerformanceReview)[] = [];
  searchTerm = '';
  loading = true;
  saving = false;
  error = '';
  successMessage = '';

  // Employee list for relations
  employeesList: Employee[] = [];

  // Modal controls
  showModal = false;
  isEdit = false;

  // Form models
  candidateForm: Partial<Candidate> = {};
  jobForm: Partial<Job> = {};
  payrollForm: {
    id?: number;
    employeeId: number | null;
    payPeriod: string;
    grossPay: number;
    deductions: number;
    netPay: number;
    processedDate: string;
  } = {
    employeeId: null,
    payPeriod: '',
    grossPay: 0,
    deductions: 0,
    netPay: 0,
    processedDate: '',
  };
  reviewForm: {
    id?: number;
    employeeId: number | null;
    reviewPeriod: string;
    rating: number;
    comments: string;
    reviewDate: string;
  } = {
    employeeId: null,
    reviewPeriod: '',
    rating: 5,
    comments: '',
    reviewDate: '',
  };

  constructor(private readonly route: ActivatedRoute, private readonly api: HrApiService, private readonly changeDetector: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.route.data.subscribe((data) => {
      this.resource = data['resource'];
      this.title = data['title'];
      this.description = data['description'];
      this.searchTerm = '';
      this.error = '';
      this.successMessage = '';
      this.closeModal();
      this.load();
      if (this.resource === 'payroll' || this.resource === 'performance') {
        this.loadEmployeesList();
      }
    });
  }

  loadEmployeesList(): void {
    this.api.getEmployees().subscribe({
      next: (emps) => {
        this.employeesList = emps;
        this.changeDetector.detectChanges();
      },
      error: () => {
        console.error('Could not load employees dropdown');
      },
    });
  }

  load(): void {
    this.loading = true;
    this.error = '';
    let request: Observable<any[]>;
    if (this.resource === 'candidates') {
      request = this.api.getCandidates();
    } else if (this.resource === 'jobs') {
      request = this.api.getJobs();
    } else if (this.resource === 'payroll') {
      request = this.api.getPayroll();
    } else {
      request = this.api.getPerformanceReviews();
    }

    request.subscribe({
      next: (rows) => {
        this.rows = rows;
        this.applyFilter();
        this.loading = false;
        this.changeDetector.detectChanges();
      },
      error: (err) => {
        this.error = err?.error?.message || `Could not load ${this.title.toLowerCase()}.`;
        this.loading = false;
        this.changeDetector.detectChanges();
      },
    });
  }

  applyFilter(): void {
    const query = this.searchTerm.toLowerCase().trim();
    if (!query) {
      this.filteredRows = [...this.rows];
      return;
    }
    this.filteredRows = this.rows.filter((row) => JSON.stringify(row).toLowerCase().includes(query));
  }

  employeeName(row: any): string {
    if (!row || !row.employee) return 'Unassigned';
    const first = row.employee.firstName || '';
    const last = row.employee.lastName || '';
    return `${first} ${last}`.trim() || 'Employee #' + row.employee.id;
  }

  openCreate(): void {
    this.isEdit = false;
    this.error = '';
    const today = new Date().toISOString().substring(0, 10);
    const monthYear = new Intl.DateTimeFormat('en-US', { month: 'long', year: 'numeric' }).format(new Date());

    if (this.resource === 'candidates') {
      this.candidateForm = {
        name: '',
        email: '',
        phone: '',
        jobTitle: '',
        status: 'Applied',
        appliedDate: today,
      };
    } else if (this.resource === 'jobs') {
      this.jobForm = {
        title: '',
        department: '',
        employmentType: 'Full-Time',
        status: 'Open',
        description: '',
      };
    } else if (this.resource === 'payroll') {
      this.payrollForm = {
        employeeId: this.employeesList.length ? this.employeesList[0].id! : null,
        payPeriod: monthYear,
        grossPay: 50000,
        deductions: 5000,
        netPay: 45000,
        processedDate: today,
      };
    } else if (this.resource === 'performance') {
      this.reviewForm = {
        employeeId: this.employeesList.length ? this.employeesList[0].id! : null,
        reviewPeriod: 'Q' + Math.ceil((new Date().getMonth() + 1) / 3) + ' ' + new Date().getFullYear(),
        rating: 5,
        comments: '',
        reviewDate: today,
      };
    }
    this.showModal = true;
  }

  openEdit(item: any): void {
    this.isEdit = true;
    this.error = '';
    if (this.resource === 'candidates') {
      this.candidateForm = { ...item };
    } else if (this.resource === 'jobs') {
      this.jobForm = { ...item };
    } else if (this.resource === 'payroll') {
      this.payrollForm = {
        id: item.id,
        employeeId: item.employee ? item.employee.id : null,
        payPeriod: item.payPeriod || '',
        grossPay: item.grossPay || 0,
        deductions: item.deductions || 0,
        netPay: item.netPay || 0,
        processedDate: item.processedDate ? item.processedDate.substring(0, 10) : '',
      };
    } else if (this.resource === 'performance') {
      this.reviewForm = {
        id: item.id,
        employeeId: item.employee ? item.employee.id : null,
        reviewPeriod: item.reviewPeriod || '',
        rating: item.rating || 5,
        comments: item.comments || '',
        reviewDate: item.reviewDate ? item.reviewDate.substring(0, 10) : '',
      };
    }
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  onPayrollPayChange(): void {
    const gross = Number(this.payrollForm.grossPay) || 0;
    const deductions = Number(this.payrollForm.deductions) || 0;
    this.payrollForm.netPay = Math.max(0, gross - deductions);
  }

  saveRecord(): void {
    this.error = '';
    this.saving = true;

    if (this.resource === 'candidates') {
      const candidate = this.candidateForm as Candidate;
      const req = this.isEdit && candidate.id
        ? this.api.updateCandidate(candidate.id, candidate)
        : this.api.createCandidate(candidate);

      req.pipe(finalize(() => { this.saving = false; })).subscribe({
        next: () => {
          this.successMessage = this.isEdit ? 'Candidate updated successfully!' : 'Candidate registered successfully!';
          this.closeModal();
          this.load();
          setTimeout(() => { this.successMessage = ''; }, 3500);
        },
        error: (err) => {
          this.error = err?.error?.message || 'Could not save candidate.';
        },
      });
    } else if (this.resource === 'jobs') {
      const job = this.jobForm as Job;
      const req = this.isEdit && job.id
        ? this.api.updateJob(job.id, job)
        : this.api.createJob(job);

      req.pipe(finalize(() => { this.saving = false; })).subscribe({
        next: () => {
          this.successMessage = this.isEdit ? 'Job position updated successfully!' : 'New job position posted successfully!';
          this.closeModal();
          this.load();
          setTimeout(() => { this.successMessage = ''; }, 3500);
        },
        error: (err) => {
          this.error = err?.error?.message || 'Could not save job.';
        },
      });
    } else if (this.resource === 'payroll') {
      if (!this.payrollForm.employeeId) {
        this.error = 'Please select an employee.';
        this.saving = false;
        return;
      }
      const payload: PayrollPayload = {
        employee: { id: this.payrollForm.employeeId },
        payPeriod: this.payrollForm.payPeriod,
        grossPay: this.payrollForm.grossPay,
        deductions: this.payrollForm.deductions,
        netPay: this.payrollForm.netPay,
        processedDate: this.payrollForm.processedDate,
      };
      const req = this.isEdit && this.payrollForm.id
        ? this.api.updatePayroll(this.payrollForm.id, payload)
        : this.api.createPayroll(payload);

      req.pipe(finalize(() => { this.saving = false; })).subscribe({
        next: () => {
          this.successMessage = this.isEdit ? 'Payroll record updated!' : 'Payroll record created!';
          this.closeModal();
          this.load();
          setTimeout(() => { this.successMessage = ''; }, 3500);
        },
        error: (err) => {
          this.error = err?.error?.message || 'Could not save payroll record.';
        },
      });
    } else if (this.resource === 'performance') {
      if (!this.reviewForm.employeeId) {
        this.error = 'Please select an employee.';
        this.saving = false;
        return;
      }
      const payload: PerformanceReviewPayload = {
        employee: { id: this.reviewForm.employeeId },
        reviewPeriod: this.reviewForm.reviewPeriod,
        rating: Number(this.reviewForm.rating),
        comments: this.reviewForm.comments,
        reviewDate: this.reviewForm.reviewDate,
      };
      const req = this.isEdit && this.reviewForm.id
        ? this.api.updatePerformanceReview(this.reviewForm.id, payload)
        : this.api.createPerformanceReview(payload);

      req.pipe(finalize(() => { this.saving = false; })).subscribe({
        next: () => {
          this.successMessage = this.isEdit ? 'Performance review updated!' : 'Performance review recorded!';
          this.closeModal();
          this.load();
          setTimeout(() => { this.successMessage = ''; }, 3500);
        },
        error: (err) => {
          this.error = err?.error?.message || 'Could not save performance review.';
        },
      });
    }
  }

  deleteItem(item: any): void {
    const label = item.name || item.title || item.payPeriod || item.reviewPeriod || 'this record';
    if (!confirm(`Are you sure you want to delete ${label}?`)) return;

    this.error = '';
    let req: Observable<void>;
    if (this.resource === 'candidates') {
      req = this.api.deleteCandidate(item.id);
    } else if (this.resource === 'jobs') {
      req = this.api.deleteJob(item.id);
    } else if (this.resource === 'payroll') {
      req = this.api.deletePayroll(item.id);
    } else {
      req = this.api.deletePerformanceReview(item.id);
    }

    req.subscribe({
      next: () => {
        this.successMessage = 'Record deleted successfully.';
        this.load();
        setTimeout(() => { this.successMessage = ''; }, 3500);
      },
      error: (err) => {
        this.error = err?.error?.message || 'Could not delete record.';
      },
    });
  }

  onboardCandidate(candidate: Candidate): void {
    if (!candidate.id || !confirm(`Onboard ${candidate.name} as a new employee?`)) return;
    this.error = '';
    this.api.onboardCandidate(candidate.id).subscribe({
      next: (emp) => {
        this.successMessage = `Success! ${candidate.name} has been onboarded as employee ${emp.employeeNumber || ''}.`;
        this.load();
        setTimeout(() => { this.successMessage = ''; }, 4000);
      },
      error: (err) => {
        this.error = err?.error?.message || 'Could not onboard candidate.';
      },
    });
  }

  // Type guards for templates
  asCandidate(item: any): Candidate { return item as Candidate; }
  asJob(item: any): Job { return item as Job; }
  asPayroll(item: any): PayrollRecord { return item as PayrollRecord; }
  asReview(item: any): PerformanceReview { return item as PerformanceReview; }
}
