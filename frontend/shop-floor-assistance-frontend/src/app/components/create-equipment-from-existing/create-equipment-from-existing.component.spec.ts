import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEquipmentFromExistingComponent } from './create-equipment-from-existing.component';

describe('CreateEquipmentFromExistingComponent', () => {
  let component: CreateEquipmentFromExistingComponent;
  let fixture: ComponentFixture<CreateEquipmentFromExistingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateEquipmentFromExistingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEquipmentFromExistingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
