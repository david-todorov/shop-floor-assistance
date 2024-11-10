import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorEditWorkflowComponent } from './editor-edit-workflow.component';

describe('EditorViewWorkflowComponent', () => {
  let component: EditorEditWorkflowComponent;
  let fixture: ComponentFixture<EditorEditWorkflowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorEditWorkflowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorEditWorkflowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
