import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AcmerAddComponent } from './acmer-add.component';

describe('AcmerAddComponent', () => {
  let component: AcmerAddComponent;
  let fixture: ComponentFixture<AcmerAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AcmerAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AcmerAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
