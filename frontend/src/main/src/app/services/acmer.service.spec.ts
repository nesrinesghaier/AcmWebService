import {TestBed} from '@angular/core/testing';

import {AcmerService} from './acmer.service';

describe('AcmerService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AcmerService = TestBed.get(AcmerService);
    expect(service).toBeTruthy();
  });
});
