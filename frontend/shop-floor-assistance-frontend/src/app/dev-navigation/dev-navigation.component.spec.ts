import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DevNavigationComponent } from './dev-navigation.component';

describe('DevNavigationComponent', () => {
  let component: DevNavigationComponent;
  let fixture: ComponentFixture<DevNavigationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DevNavigationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DevNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
