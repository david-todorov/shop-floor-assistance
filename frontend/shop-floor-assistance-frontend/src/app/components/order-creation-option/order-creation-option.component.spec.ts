import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderCreationOptionComponent } from './order-creation-option.component';

describe('OrderCreationOptionComponent', () => {
  let component: OrderCreationOptionComponent;
  let fixture: ComponentFixture<OrderCreationOptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderCreationOptionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderCreationOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
