import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaregiverOverviewComponent } from './caregiver-overview.component';

describe('CaregiverOverviewComponent', () => {
  let component: CaregiverOverviewComponent;
  let fixture: ComponentFixture<CaregiverOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CaregiverOverviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CaregiverOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
