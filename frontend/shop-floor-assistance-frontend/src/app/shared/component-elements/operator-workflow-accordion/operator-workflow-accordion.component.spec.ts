import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperatorWorkflowAccordionComponent } from './operator-workflow-accordion.component';

describe('OperatorWorkflowAccordionComponent', () => {
  let component: OperatorWorkflowAccordionComponent;
  let fixture: ComponentFixture<OperatorWorkflowAccordionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OperatorWorkflowAccordionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperatorWorkflowAccordionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
