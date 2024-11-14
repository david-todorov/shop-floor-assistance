import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCreationOptionComponent } from './product-creation-option.component';

describe('ProductCreationOptionComponent', () => {
  let component: ProductCreationOptionComponent;
  let fixture: ComponentFixture<ProductCreationOptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductCreationOptionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductCreationOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
