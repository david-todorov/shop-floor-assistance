import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperatorViewWorkflowComponent } from './operator-view-workflow.component';

describe('OperatorViewWorkflowComponent', () => {
  let component: OperatorViewWorkflowComponent;
  let fixture: ComponentFixture<OperatorViewWorkflowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OperatorViewWorkflowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperatorViewWorkflowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
