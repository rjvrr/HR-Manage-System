import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { timeout } from 'rxjs';
import { HrApiService } from '../services/hr-api';

@Component({
  selector: 'app-layout',
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class Layout {
  showSettings = false;
  backendHealth: string = 'Checking...';

  constructor(private readonly api: HrApiService, private readonly changeDetector: ChangeDetectorRef) {}

  openSettings(): void {
    this.showSettings = true;
    this.backendHealth = 'Checking...';
    this.api.getHealth().pipe(timeout({ first: 5000 })).subscribe({
      next: (h) => {
        this.backendHealth = `${h.status} (${h.service} - ${h.domain})`;
        this.changeDetector.detectChanges();
      },
      error: () => {
        this.backendHealth = 'OFFLINE / UNREACHABLE';
        this.changeDetector.detectChanges();
      }
    });
  }

  closeSettings(): void {
    this.showSettings = false;
  }
}