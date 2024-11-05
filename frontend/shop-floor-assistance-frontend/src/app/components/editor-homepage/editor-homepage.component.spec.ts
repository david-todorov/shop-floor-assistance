import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorHomepageComponent } from './editor-homepage.component';

describe('EditorHomepageComponent', () => {
  let component: EditorHomepageComponent;
  let fixture: ComponentFixture<EditorHomepageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorHomepageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorHomepageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
