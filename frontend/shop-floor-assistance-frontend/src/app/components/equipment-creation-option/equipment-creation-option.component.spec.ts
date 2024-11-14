import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquipmentCreationOptionComponent } from './equipment-creation-option.component';

describe('EquipmentCreationOptionComponent', () => {
  let component: EquipmentCreationOptionComponent;
  let fixture: ComponentFixture<EquipmentCreationOptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EquipmentCreationOptionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EquipmentCreationOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
