import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorEditProductComponent } from './editor-edit-product.component';

describe('EditorEditProductComponent', () => {
  let component: EditorEditProductComponent;
  let fixture: ComponentFixture<EditorEditProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorEditProductComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorEditProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
