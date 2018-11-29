import { AcmerModule } from './acmer.module';

describe('AcmerModule', () => {
  let acmerModule: AcmerModule;

  beforeEach(() => {
    acmerModule = new AcmerModule();
  });

  it('should create an instance', () => {
    expect(acmerModule).toBeTruthy();
  });
});
