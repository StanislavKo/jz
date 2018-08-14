import { Component, OnInit, Input, ViewChild } from '@angular/core';

import { MatProgressBarModule } from '@angular/material/progress-bar';

import { MainComponent } from '../main/main.component';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  @ViewChild(MatProgressBarModule) progressBar: MatProgressBarModule; 

  @Input() parent: MainComponent;
  @Input() isLoading: boolean = false;
  public query: string = "";

  constructor() { }

  ngOnInit() {
    //this.progressBar
  }

  public queryChanged(newQuery: string) {
    this.query = newQuery;
  } 

  public searchClick(event) {
    if (this.query.length > 0) {
      this.parent["loadData"](this.query);
    }
  } 

}
