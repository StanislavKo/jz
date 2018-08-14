import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { FormControl } from '@angular/forms';
import { URLSearchParams } from '@angular/http';
import 'rxjs/add/operator/map';  
import { MatTabsModule, MatTabGroup } from '@angular/material/tabs';

import { SignstatusComponent } from '../signstatus/signstatus.component';  
import { SearchComponent } from '../search/search.component';  
import { SearchtermsComponent } from '../searchterms/searchterms.component';  
import { TitlelistComponent } from '../titlelist/titlelist.component';  
import { PaginationComponent } from '../pagination/pagination.component';  
import { DescriptionComponent } from '../description/description.component';  

import { BackendService } from '../backend.service';  

import { environment } from '../../environments/environment';

import { Constants } from "../constants";
import { MyCookie } from "../cookie";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  providers: [BackendService]
})

export class MainComponent implements OnInit {

  public ACTIVE_VIEW = {EPISODES: 0, LOGIN: 1};
  public ACTIVE_SEARCH_FAVORITE = {SEARCH: 0, FAVORITE: 1};

  @ViewChild(SignstatusComponent) signstatusComponent: SignstatusComponent; 
  @ViewChild(SearchComponent) searchComponent: SearchComponent; 
  @ViewChild(SearchtermsComponent) searchtermsComponent: SearchtermsComponent; 
  @ViewChild(DescriptionComponent) descriptionComponent: DescriptionComponent; 
  @ViewChild(MatTabGroup) searchFavoriteTabComponent: MatTabGroup; 

  private rootUrl = environment.rootUrl;

  public isEpisodesLoading: boolean = false;
  public episodes: Array<any> = [];
  public titles: Array<Object> = [];
  public episodeSelected: Object;
  public el_offset: number;
  public el_limit: number;
  public el_count: number;

  public episodesFavorite: Array<any> = [];
  public titlesFavorite: Array<Object> = [];
  public fel_offset: number;
  public fel_limit: number;
  public fel_count: number;

  public activeView = this.ACTIVE_VIEW.EPISODES;
  public activeSearchFavorite = this.ACTIVE_SEARCH_FAVORITE.SEARCH;
  public innerWidth: number;
  private query: string;
  private isOnceLoaded: boolean = false;

  constructor(private _backendService: BackendService, private cdRef:ChangeDetectorRef) { }

  ngOnInit() {
    this._backendService.setParent(this);
    this.innerWidth = window.innerWidth;

    const paramState = this.getQueryParameter("state");
    const paramJwt = this.getQueryParameter("jwt");
    console.log("onInit_01");
    console.log("onInit_01 state=" + paramState);
    console.log("onInit_01 jwt=" + paramJwt);
    console.log("onInit_01 search=" + window.location.search);
    if (MyCookie.getCookie("oauth2_state") && paramState && MyCookie.getCookie("oauth2_state") == paramState && paramJwt) {
      console.log("onInit_02");
      MyCookie.setCookie("jwt", "Bearer " + paramJwt, 60);
    }
    if (paramState && paramJwt) {
      window.location.href = this.rootUrl;
    }
  }

  ngAfterViewInit() {
    if (MyCookie.getCookie("jwt")) {
      this.loadFavorites();
    }
  }

  public onResultFavoritesChanged(event) {
    if (event == 0) {
      this.selectSearch();
    } else if (event == 1) {
      this.selectFavorite();
    }
  }

  public signin() {
    this.activeView = this.ACTIVE_VIEW.LOGIN;
  }

  public onSignin() {
    this.activeView = this.ACTIVE_VIEW.EPISODES;
    if (this.searchtermsComponent) {
      this.searchtermsComponent.updateList();
    }
    if (this.descriptionComponent) {
      this.descriptionComponent.calcIsSigninIn();
    }
    this.loadFavorites();
  }

  public onSignout() {
    this.signstatusComponent.calcIsSigninIn();
    this.searchtermsComponent.updateList();
    if (this.descriptionComponent) {
      this.descriptionComponent.calcIsSigninIn();
    }
    if (this.searchComponent) {
      this.searchComponent.queryChanged("");
    }
    this.episodesFavorite.length = 0;
    this.titlesFavorite.length = 0;
    this.fel_offset = null;
    this.fel_limit = null;
    this.fel_count = null;
  }

  public onFavoriteChanged() {
    this.loadFavorites();
  }

  public loadData(query: string, offset: number = 0, limit: number = Constants.PAGE_SIZE): void {
    this.query = query;
    this.searchComponent.queryChanged(query);

    this.isEpisodesLoading = true;
    this._backendService.getEpisodeList(query, offset, limit).subscribe(response => {
      let data = response["data"];
      let count = response["count"];

      this.isEpisodesLoading = false;
      this.episodes.length = 0;
      this.episodes.push(...data);
      this.titles = this.episodes.map((episode: Object) => {
        return {hash: episode["hash"], title:episode["title"], created:episode["created"]};
      });
      this.el_offset = offset;
      this.el_limit = limit;
      this.el_count = count;

      this.searchtermsComponent.updateList();
      this.activeSearchFavorite = this.ACTIVE_SEARCH_FAVORITE.SEARCH;
      this.searchFavoriteTabComponent.selectedIndex = 0;
      this.isOnceLoaded = true;
    }); 
  } 

  public paginateData(offset: number, limit: number): void {
    this.loadData(this.query, offset, limit);
  }

  public paginateDataFav(offset: number, limit: number): void {
    this.titlesFavorite = this.episodesFavorite.slice(offset, offset + limit).map((episode: Object) => {
      return {hash: episode["hash"], title:episode["title"], created:episode["created"]};
    });
    this.fel_offset = offset;
    this.fel_limit = limit;
  }

  public showEpisode(hash: string): void {
    var episode = this.episodes.filter(e => e["hash"] == hash)[0];
    if (episode) {
    } else {
      episode = this.episodesFavorite.filter(e => e["hash"] == hash)[0];
    }
    this.episodeSelected = episode;
  } 

  private getQueryParameter(key: string): string {
    const parameters = new URLSearchParams(window.location.search.slice(1));
    return parameters.get(key);
  }

  public selectSearch() {
    this.activeSearchFavorite = this.ACTIVE_SEARCH_FAVORITE.SEARCH;
  }

  public selectFavorite() {
    this.activeSearchFavorite = this.ACTIVE_SEARCH_FAVORITE.FAVORITE;
  }

  public loadFavorites() {
    this._backendService.getFavoriteEpisodeList().subscribe(response => {
      let dataFavorite = response["data"];
      let countFavorite = response["count"];

      this.episodesFavorite.length = 0;
      this.episodesFavorite.push(...dataFavorite);
      this.titlesFavorite = this.episodesFavorite.slice(0, 10).map((episode: Object) => {
        return {hash: episode["hash"], title:episode["title"], created:episode["created"]};
      });
      this.fel_offset = 0;
      this.fel_limit = 10;
      this.fel_count = countFavorite;
    }); 
  }

  public isSignedIn(): boolean {
    let jwt = MyCookie.getCookie("jwt");
    if (jwt) {
      return true;
    } else {
      return false;
    }
  } 

}
