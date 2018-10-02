import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AcmerListComponent } from './acmer-list.component';

describe('AcmerListComponent', () => {
  let component: AcmerListComponent;
  let fixture: ComponentFixture<AcmerListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AcmerListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AcmerListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
