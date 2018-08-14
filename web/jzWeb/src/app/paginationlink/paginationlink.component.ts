import { Component, OnInit, Input } from '@angular/core';

import { MainComponent } from '../main/main.component'; 

import { Constants } from "../constants";

@Component({
  selector: 'app-paginationlink',
  templateUrl: './paginationlink.component.html',
  styleUrls: ['./paginationlink.component.css']
})
export class PaginationlinkComponent implements OnInit {

  @Input() parent: MainComponent; 
  @Input() type: number;
  @Input() isVisible: boolean = false; 
  @Input() isCurrent: boolean = false; 
  @Input() title: number = 1;

  constructor() { }

  ngOnInit() {
  }

  public doAction() {
    if (this.type == 0) {
      this.parent.paginateData((this.title - 1)*Constants.PAGE_SIZE, Constants.PAGE_SIZE);
    } else if (this.type == 1) {
      this.parent.paginateDataFav((this.title - 1)*Constants.PAGE_SIZE, Constants.PAGE_SIZE);
    }
  }

}
