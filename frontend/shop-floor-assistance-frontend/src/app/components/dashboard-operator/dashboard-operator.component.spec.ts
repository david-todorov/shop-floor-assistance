import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardOperatorComponent } from './dashboard-operator.component';

describe('DashboardOperatorComponent', () => {
  let component: DashboardOperatorComponent;
  let fixture: ComponentFixture<DashboardOperatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardOperatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardOperatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
