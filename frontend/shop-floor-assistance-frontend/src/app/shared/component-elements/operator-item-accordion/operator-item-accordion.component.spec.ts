import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperatorItemAccordionComponent } from './operator-item-accordion.component';

describe('OperatorItemAccordionComponent', () => {
  let component: OperatorItemAccordionComponent;
  let fixture: ComponentFixture<OperatorItemAccordionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OperatorItemAccordionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperatorItemAccordionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
