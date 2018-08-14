import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchtermsComponent } from './searchterms.component';

describe('SearchtermsComponent', () => {
  let component: SearchtermsComponent;
  let fixture: ComponentFixture<SearchtermsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchtermsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchtermsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
