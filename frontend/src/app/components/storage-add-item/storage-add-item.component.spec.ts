import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StorageAddItemComponent } from './storage-add-item.component';

describe('StorageAddItemComponent', () => {
  let component: StorageAddItemComponent;
  let fixture: ComponentFixture<StorageAddItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StorageAddItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StorageAddItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
