import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorCreateProductComponent } from './editor-create-product.component';

describe('EditorCreateProductComponent', () => {
  let component: EditorCreateProductComponent;
  let fixture: ComponentFixture<EditorCreateProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorCreateProductComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorCreateProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
