import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorAccordionComponent } from './editor-accordion.component';

describe('EditorAccordionComponent', () => {
  let component: EditorAccordionComponent;
  let fixture: ComponentFixture<EditorAccordionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorAccordionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorAccordionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
