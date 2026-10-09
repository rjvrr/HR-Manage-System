import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { Employee, HrApiService } from '../services/hr-api';

@Component({
  selector: 'app-employees',
  imports: [CommonModule, FormsModule],
  templateUrl: './employees.html',
  styleUrl: './employees.css',
})
export class Employees implements OnInit {
  employees: Employee[] = [];
  filteredEmployees: Employee[] = [];
  selectedEmployee: Employee | null = null;
  searchTerm = '';
  departmentFilter = 'All departments';
  showForm = false;
  saving = false;
  loading = true;
  error = '';
  successMessage = '';

  constructor(private readonly api: HrApiService, private readonly changeDetector: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.error = '';
    this.loading = true;
    this.api.getEmployees().subscribe({
      next: (employees) => {
        this.employees = employees;
        this.applyFilters();
        this.loading = false;
        this.changeDetector.detectChanges();
      },
      error: (err) => {
        this.error = err?.error?.message || 'Unable to load employees. Make sure backend is running.';
        this.loading = false;
        this.changeDetector.detectChanges();
      },
    });
  }

  get departments(): string[] {
    const depts = new Set(this.employees.map((e) => e.department).filter(Boolean));
    return ['All departments', ...depts];
  }

  applyFilters(): void {
    const query = this.searchTerm.trim().toLowerCase();
    this.filteredEmployees = this.employees.filter((employee) => {
      const name = `${employee.firstName || ''} ${employee.lastName || ''}`.toLowerCase();
      const job = (employee.jobTitle || '').toLowerCase();
      const email = (employee.email || '').toLowerCase();
      const empNo = (employee.employeeNumber || '').toLowerCase();
      const matchesSearch = !query || name.includes(query) || job.includes(query) || email.includes(query) || empNo.includes(query);
      const matchesDepartment = this.departmentFilter === 'All departments' || employee.department === this.departmentFilter;
      return matchesSearch && matchesDepartment;
    });
  }

  getInitials(employee: Employee): string {
    const first = (employee.firstName || '').trim().charAt(0);
    const last = (employee.lastName || '').trim().charAt(0);
    return (first + last).toUpperCase() || 'EM';
  }

  openCreate(): void {
    this.selectedEmployee = {
      employeeNumber: 'EMP' + Math.floor(1000 + Math.random() * 9000),
      firstName: '',
      lastName: '',
      email: '',
      phone: '',
      department: '',
      jobTitle: '',
      employmentStatus: 'Active',
      hireDate: new Date().toISOString().substring(0, 10),
      annualSalary: 0,
    };
    this.showForm = true;
  }

  openEdit(employee: Employee): void {
    this.selectedEmployee = { ...employee };
    this.showForm = true;
  }

  closeForm(): void {
    this.showForm = false;
    this.selectedEmployee = null;
  }

  saveEmployee(): void {
    if (!this.selectedEmployee) return;
    this.error = '';
    this.saving = true;
    const employee = { ...this.selectedEmployee };
    const request = employee.id
      ? this.api.updateEmployee(employee.id, employee)
      : this.api.createEmployee(employee);

    request.pipe(finalize(() => { this.saving = false; })).subscribe({
      next: (savedEmployee) => {
        this.successMessage = employee.id ? 'Employee updated successfully!' : 'Employee added successfully!';
        setTimeout(() => { this.successMessage = ''; }, 3500);
        this.closeForm();
        this.loadEmployees();
      },
      error: (err) => {
        this.error = err?.error?.message || 'Could not save this employee.';
      },
    });
  }

  deleteEmployee(employee: Employee): void {
    if (!employee.id || !confirm(`Remove ${employee.firstName} ${employee.lastName}?`)) return;
    this.error = '';
    this.api.deleteEmployee(employee.id).subscribe({
      next: () => {
        this.successMessage = 'Employee removed successfully!';
        setTimeout(() => { this.successMessage = ''; }, 3500);
        this.loadEmployees();
      },
      error: (err) => {
        this.error = err?.error?.message || 'Could not remove this employee.';
      },
    });
  }
}