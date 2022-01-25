import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UnitOfQuantityComponent} from './unit-of-quantity.component';

describe('UnitOfQuantityComponent', () => {
  let component: UnitOfQuantityComponent;
  let fixture: ComponentFixture<UnitOfQuantityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UnitOfQuantityComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UnitOfQuantityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
