import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorCreateOrderComponent } from './editor-create-order.component';

describe('OrderFormComponent', () => {
  let component: EditorCreateOrderComponent;
  let fixture: ComponentFixture<EditorCreateOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorCreateOrderComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(EditorCreateOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
