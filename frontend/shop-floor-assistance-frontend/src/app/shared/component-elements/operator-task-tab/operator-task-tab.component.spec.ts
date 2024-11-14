import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperatorTaskTabComponent } from './operator-task-tab.component';

describe('OperatorTaskTabComponent', () => {
  let component: OperatorTaskTabComponent;
  let fixture: ComponentFixture<OperatorTaskTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OperatorTaskTabComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperatorTaskTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
