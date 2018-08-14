import { Component, OnInit, Input } from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';

import { MainComponent } from '../main/main.component';

import { BackendService } from '../backend.service';  

import { MyCookie } from "../cookie"; 

@Component({
  selector: 'app-searchterms',
  templateUrl: './searchterms.component.html',
  styleUrls: ['./searchterms.component.css']
})
export class SearchtermsComponent implements OnInit {

  @Input() parent: MainComponent;
  public searchTerms: Array<string> = [];

  private eraseClickTime: number = 0;

  constructor(private _backendService: BackendService) { }

  ngOnInit() {
    this.updateList();
  }

  public updateList() {
    let jwt = MyCookie.getCookie("jwt");
    if (jwt) { 
      this._backendService.getSearchTerms().subscribe(
        response => {
          this.searchTerms.length = 0;
          this.searchTerms.push(...response); 
        },
        error => {
          this.searchTerms.length = 0;
        }
      );
    } else {
      this.searchTerms.length = 0;
    }
  }

  public doSearch(query) {
    if (query.length > 0 && (new Date().getTime() - this.eraseClickTime > 300)) {
      this.parent["loadData"](query);
    }
  } 

  public doErase(query) {
    this.eraseClickTime = new Date().getTime();
    if (query.length > 0) {
      this._backendService.deleteSearchTerm(query).subscribe(
        response => {
          this.updateList();
        },
        error => {
        }
      );
    }
  } 

}
