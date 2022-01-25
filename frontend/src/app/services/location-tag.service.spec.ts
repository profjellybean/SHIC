import {TestBed} from '@angular/core/testing';

import {LocationTagService} from './location-tag.service';

describe('LocationTagService', () => {
  let service: LocationTagService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocationTagService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
