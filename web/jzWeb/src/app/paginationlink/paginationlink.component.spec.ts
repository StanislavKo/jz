import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginationlinkComponent } from './paginationlink.component';

describe('PaginationlinkComponent', () => {
  let component: PaginationlinkComponent;
  let fixture: ComponentFixture<PaginationlinkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaginationlinkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaginationlinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
