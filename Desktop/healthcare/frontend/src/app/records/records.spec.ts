import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Records } from './records';

describe('Records', () => {
  let component: Records;
  let fixture: ComponentFixture<Records>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Records],
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({ resource: 'candidates', title: 'Recruitment', description: 'Track candidates' }),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Records);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with route data', () => {
    expect(component.resource).toBe('candidates');
    expect(component.title).toBe('Recruitment');
  });

  it('should open and close create modal', () => {
    component.openCreate();
    expect(component.showModal).toBe(true);
    expect(component.isEdit).toBe(false);
    component.closeModal();
    expect(component.showModal).toBe(false);
  });
});
