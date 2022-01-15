import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmUserEmailChangedComponent } from './confirm-user-email-changed.component';

describe('ConfirmUserEmailChangedComponent', () => {
  let component: ConfirmUserEmailChangedComponent;
  let fixture: ComponentFixture<ConfirmUserEmailChangedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmUserEmailChangedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmUserEmailChangedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
