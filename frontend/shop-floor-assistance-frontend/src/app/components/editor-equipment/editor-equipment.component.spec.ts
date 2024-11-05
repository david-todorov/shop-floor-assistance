import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorEquipmentComponent } from './editor-equipment.component';

describe('EditorEquipmentComponent', () => {
  let component: EditorEquipmentComponent;
  let fixture: ComponentFixture<EditorEquipmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorEquipmentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorEquipmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
