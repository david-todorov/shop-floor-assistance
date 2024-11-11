import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorEditOrderComponent } from './editor-edit-order.component';

describe('EditorViewWorkflowComponent', () => {
  let component: EditorEditOrderComponent;
  let fixture: ComponentFixture<EditorEditOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorEditOrderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorEditOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
