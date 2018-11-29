import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AcmerEditComponent } from './acmer-edit.component';

describe('AcmerEditComponent', () => {
  let component: AcmerEditComponent;
  let fixture: ComponentFixture<AcmerEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AcmerEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AcmerEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
