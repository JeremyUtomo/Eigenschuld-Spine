import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaregiverNavbarComponent } from './caregiver-navbar.component';

describe('CaregiverNavbarComponent', () => {
  let component: CaregiverNavbarComponent;
  let fixture: ComponentFixture<CaregiverNavbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CaregiverNavbarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CaregiverNavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
