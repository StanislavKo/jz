import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Http, Response, HttpModule } from '@angular/http'; 

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatProgressBarModule } from '@angular/material';
import { MatTabsModule } from '@angular/material/tabs';
import { MatChipsModule } from '@angular/material/chips';
import { ThemePalette } from '@angular/material/core';

import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
import { SearchComponent } from './search/search.component';
import { TitlelistComponent } from './titlelist/titlelist.component';
import { DescriptionComponent } from './description/description.component';
import { PaginationComponent } from './pagination/pagination.component';
import { PaginationlinkComponent } from './paginationlink/paginationlink.component';
import { SignstatusComponent } from './signstatus/signstatus.component';
import { SignupComponent } from './signup/signup.component';
import { SigninComponent } from './signin/signin.component';
import { SearchtermsComponent } from './searchterms/searchterms.component';
import { EpisodeComponent } from './episode/episode.component';

import { routing } from './app.routing';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    TitlelistComponent,
    DescriptionComponent,
    PaginationComponent,
    PaginationlinkComponent,
    SignstatusComponent,
    SignupComponent,
    SigninComponent,
    SearchtermsComponent,
    EpisodeComponent,
    MainComponent,
  ],
  imports: [
    BrowserModule,
    HttpModule,
    BrowserAnimationsModule,
    MatProgressBarModule,
    MatTabsModule,
    MatChipsModule,
    routing,
  ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
