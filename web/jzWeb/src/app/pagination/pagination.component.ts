import { Component, OnInit, OnChanges, SimpleChanges, ViewChild, Input } from '@angular/core';

import { AppComponent } from '../app.component'; 

import { PaginationlinkComponent } from '../paginationlink/paginationlink.component';  

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnChanges, OnInit {

  @Input() parent: AppComponent; 
  @Input() type: number; 
  @Input() offset: number; 
  @Input() limit: number; 
  @Input() count: number; 

  @ViewChild(PaginationlinkComponent) link1: PaginationlinkComponent; 
  @ViewChild(PaginationlinkComponent) link2: PaginationlinkComponent; 
  @ViewChild(PaginationlinkComponent) link3: PaginationlinkComponent; 
  @ViewChild(PaginationlinkComponent) link4: PaginationlinkComponent; 
  @ViewChild(PaginationlinkComponent) link5: PaginationlinkComponent; 
  @ViewChild(PaginationlinkComponent) link6: PaginationlinkComponent; 
  @ViewChild(PaginationlinkComponent) link7: PaginationlinkComponent; 

  link1IsVisible: boolean = false;
  link2IsVisible: boolean = false;
  link3IsVisible: boolean = false;
  link4IsVisible: boolean = false;
  link5IsVisible: boolean = false;
  link6IsVisible: boolean = false;
  link7IsVisible: boolean = false;
  link1IsCurrent: boolean = false;
  link2IsCurrent: boolean = false;
  link3IsCurrent: boolean = false;
  link4IsCurrent: boolean = false;
  link5IsCurrent: boolean = false;
  link6IsCurrent: boolean = false;
  link7IsCurrent: boolean = false;
  link1Title: number = 1;
  link2Title: number = 1;
  link3Title: number = 1;
  link4Title: number = 1;
  link5Title: number = 1;
  link6Title: number = 1;
  link7Title: number = 1;

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
    console.log("ngOnChanges");
    this.link1IsVisible = false;
    this.link2IsVisible = false;
    this.link3IsVisible = false;
    this.link4IsVisible = false;
    this.link5IsVisible = false;
    this.link6IsVisible = false;
    this.link7IsVisible = false;
    if (this.count > 0) {
      let index = Math.floor(this.offset/this.limit) + 1;
      let indexMin = Math.max(1, index - 3);
      let indexMax = Math.min(indexMin + 6, Math.ceil(this.count/this.limit));
      console.log("ngOnChanges [index:" + index + "] [indexMin:" + indexMin + "] [indexMax:" + indexMax + "]");
      for (var i = 0; i <= (indexMax - indexMin); i++) {
        if (i == 0) {
          this.link1IsVisible = true;
          this.link1Title = (indexMin + i);
          this.link1IsCurrent = index == (indexMin + i);
        } else if (i == 1) {
          this.link2IsVisible = true;
          this.link2Title = (indexMin + i);
          this.link2IsCurrent = index == (indexMin + i);
        } else if (i == 2) {
          this.link3IsVisible = true;
          this.link3Title = (indexMin + i);
          this.link3IsCurrent = index == (indexMin + i);
        } else if (i == 3) {
          this.link4IsVisible = true;
          this.link4Title = (indexMin + i);
          this.link4IsCurrent = index == (indexMin + i);
        } else if (i == 4) {
          this.link5IsVisible = true;
          this.link5Title = (indexMin + i);
          this.link5IsCurrent = index == (indexMin + i);
        } else if (i == 5) {
          this.link6IsVisible = true;
          this.link6Title = (indexMin + i);
          this.link6IsCurrent = index == (indexMin + i);
        } else if (i == 6) {
          this.link7IsVisible = true;
          this.link7Title = (indexMin + i);
          this.link7IsCurrent = index == (indexMin + i);
        }
      }
    }
  }

}
