import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Analytics, HrApiService } from '../services/hr-api';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  analytics: Analytics | null = null;
  loading = true;
  error = '';
  readonly today = new Intl.DateTimeFormat('en-US', {
    weekday: 'long',
    month: 'long',
    day: 'numeric',
    year: 'numeric',
  }).format(new Date()).toUpperCase();

  constructor(private readonly api: HrApiService, private readonly changeDetector: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.api.getAnalytics().subscribe({
      next: (analytics) => {
        this.analytics = analytics;
        this.loading = false;
        this.changeDetector.detectChanges();
      },
      error: () => {
        this.error = 'Analytics are unavailable. Check that the backend is running.';
        this.loading = false;
        this.changeDetector.detectChanges();
      },
    });
  }

  get departments(): { name: string; count: number; percent: number }[] {
    if (!this.analytics) return [];
    const entries = Object.entries(this.analytics.employeesByDepartment);
    return entries.map(([name, count]) => ({ name, count, percent: this.analytics!.totalEmployees ? count / this.analytics!.totalEmployees * 100 : 0 }));
  }
}
