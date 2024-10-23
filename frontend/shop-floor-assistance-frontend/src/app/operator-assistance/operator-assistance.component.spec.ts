import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperatorAssistanceComponent } from './operator-assistance.component';

describe('OperatorAssistanceComponent', () => {
  let component: OperatorAssistanceComponent;
  let fixture: ComponentFixture<OperatorAssistanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OperatorAssistanceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperatorAssistanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
