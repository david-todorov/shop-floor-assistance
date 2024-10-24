import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectOrderEquipmentComponent } from './select-order-equipment.component';

describe('SelectOrderEquipmentComponent', () => {
  let component: SelectOrderEquipmentComponent;
  let fixture: ComponentFixture<SelectOrderEquipmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectOrderEquipmentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectOrderEquipmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
