import { ComponentFixture, TestBed } from '@angular/core/testing';

<<<<<<<< HEAD:frontend/shop-floor-assistance-frontend/src/app/shared/component-elements/task-tab/task-tab.component.spec.ts
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
========
import { LoginRegisterComponent } from './login-register.component';

describe('LoginRegisterComponent', () => {
  let component: LoginRegisterComponent;
  let fixture: ComponentFixture<LoginRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginRegisterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginRegisterComponent);
>>>>>>>> SFA-56-frontend-CRUD-for-orders:frontend/shop-floor-assistance-frontend/src/app/components/login-register/login-register.component.spec.ts
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
