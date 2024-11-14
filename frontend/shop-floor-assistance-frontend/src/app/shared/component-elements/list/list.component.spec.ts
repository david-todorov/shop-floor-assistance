import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskTabComponent } from './task-tab.component';

describe('TaskTabComponent', () => {
  let component: TaskTabComponent;
  let fixture: ComponentFixture<TaskTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskTabComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
