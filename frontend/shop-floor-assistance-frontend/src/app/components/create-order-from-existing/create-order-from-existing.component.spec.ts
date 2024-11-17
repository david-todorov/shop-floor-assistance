import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateOrderFromExistingComponent } from './create-order-from-existing.component';

describe('CreateOrderFromExistingComponent', () => {
  let component: CreateOrderFromExistingComponent;
  let fixture: ComponentFixture<CreateOrderFromExistingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateOrderFromExistingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateOrderFromExistingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
