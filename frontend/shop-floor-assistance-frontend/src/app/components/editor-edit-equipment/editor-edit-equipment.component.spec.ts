import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorEditEquipmentComponent } from './editor-edit-equipment.component';

describe('EditorEditEquipmentComponent', () => {
  let component: EditorEditEquipmentComponent;
  let fixture: ComponentFixture<EditorEditEquipmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorEditEquipmentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorEditEquipmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
