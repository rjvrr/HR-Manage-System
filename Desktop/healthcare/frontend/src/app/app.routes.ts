import { Routes } from '@angular/router';
import { Dashboard } from './dashboard/dashboard';
import { Employees } from './employees/employees';
import { Records } from './records/records';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    component: Dashboard
  },
  {
    path: 'employees',
    component: Employees
  },
  {
    path: 'candidates',
    component: Records,
    data: { resource: 'candidates', title: 'Recruitment', description: 'Track candidates from first contact to offer.' }
  },
  {
    path: 'jobs',
    component: Records,
    data: { resource: 'jobs', title: 'Open roles', description: 'Keep hiring priorities visible and moving.' }
  },
  {
    path: 'payroll',
    component: Records,
    data: { resource: 'payroll', title: 'Payroll', description: 'Review processed payroll and take care of the details.' }
  },
  {
    path: 'performance',
    component: Records,
    data: { resource: 'performance', title: 'Performance', description: 'See review history and celebrate progress.' }
  }
];