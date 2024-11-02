import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorViewWorkflowComponent } from './editor-view-workflow.component';

describe('EditorViewWorkflowComponent', () => {
  let component: EditorViewWorkflowComponent;
  let fixture: ComponentFixture<EditorViewWorkflowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorViewWorkflowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorViewWorkflowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
