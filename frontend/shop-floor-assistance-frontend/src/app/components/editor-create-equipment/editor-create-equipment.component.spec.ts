import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorCreateEquipmentComponent } from './editor-create-equipment.component';

describe('EditorCreateEquipmentComponent', () => {
  let component: EditorCreateEquipmentComponent;
  let fixture: ComponentFixture<EditorCreateEquipmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorCreateEquipmentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorCreateEquipmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});