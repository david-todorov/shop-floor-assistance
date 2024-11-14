import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateProductFromExistingComponent } from './create-product-from-existing.component';

describe('CreateProductFromExistingComponent', () => {
  let component: CreateProductFromExistingComponent;
  let fixture: ComponentFixture<CreateProductFromExistingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateProductFromExistingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateProductFromExistingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
