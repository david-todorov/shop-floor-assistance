import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorCreateWorkflowComponent } from './editor-create-workflow.component';

describe('EditorCreateWorkflowComponent', () => {
  let component: EditorCreateWorkflowComponent;
  let fixture: ComponentFixture<EditorCreateWorkflowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorCreateWorkflowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorCreateWorkflowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
