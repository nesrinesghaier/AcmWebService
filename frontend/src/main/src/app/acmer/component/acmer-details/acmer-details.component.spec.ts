import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AcmerDetailsComponent } from './acmer-details.component';

describe('AcmerDetailsComponent', () => {
  let component: AcmerDetailsComponent;
  let fixture: ComponentFixture<AcmerDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AcmerDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AcmerDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
