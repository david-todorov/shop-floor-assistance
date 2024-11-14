import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorProductComponent } from './editor-product.component';

describe('EditorProductComponent', () => {
  let component: EditorProductComponent;
  let fixture: ComponentFixture<EditorProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorProductComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
