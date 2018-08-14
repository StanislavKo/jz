import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SignstatusComponent } from './signstatus.component';

describe('SignstatusComponent', () => {
  let component: SignstatusComponent;
  let fixture: ComponentFixture<SignstatusComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SignstatusComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignstatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
