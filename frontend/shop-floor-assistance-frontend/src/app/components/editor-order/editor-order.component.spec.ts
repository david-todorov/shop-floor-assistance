import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorOrderComponent } from './editor-order.component';

describe('EditorComponent', () => {
  let component: EditorOrderComponent;
  let fixture: ComponentFixture<EditorOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorOrderComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(EditorOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});