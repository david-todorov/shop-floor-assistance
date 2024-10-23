import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpansionComponent } from './expansion.component';

describe('ExpansionComponent', () => {
  let component: ExpansionComponent;
  let fixture: ComponentFixture<ExpansionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpansionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExpansionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
