import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http'; 
import { Observable } from 'rxjs/Observable'; 
import 'rxjs/add/operator/map'; 

import { environment } from '../environments/environment';

import { MainComponent } from './main/main.component'; 

import { MyCookie } from "./cookie";

@Injectable()
export class BackendService {

  private signinUrl = environment.signinUrl;
  private signupUrl = environment.signupUrl;
  private episodesUrl = environment.episodesUrl;
  private searchTermsUrl = environment.searchTermsUrl;
  private deleteSearchTermUrl = environment.deleteSearchTermUrl;
  private addFavoriteUrl = environment.addFavoriteUrl;
  private favoritesUrl = environment.favoritesUrl;
  private deleteFavoriteUrl = environment.deleteFavoriteUrl;
  private episodeUrl = environment.episodeUrl;

  private mainParent: MainComponent;

  public constructor(public http: Http) { }  

  public setParent(mainParent: MainComponent) {
    this.mainParent = mainParent;
  }

  public signin(username: string, password: string): Observable<any> {
    let parameter = JSON.stringify({username:username, password:password});
    return this.http.post(this.signinUrl, parameter)
      .map((res:Response) => {
        let jwt = res.headers.get('Jwt');
        console.log("signin jwt=" + jwt);
        if (jwt) {
          MyCookie.setCookie("jwt", jwt, 60);
        }
        return "";
      })
      .catch((error:any) => Observable.throw(error.json().error || 'Ошибка на сервере'));
  } 

  public signup(username: string, password: string): Observable<any> {
    let parameter = JSON.stringify({username:username, password:password});
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return this.http.post(this.signupUrl, parameter, {headers: headers})
      .map((res:Response) => {
        let jwt = res.headers.get('Authorization');
        if (jwt) {
          MyCookie.setCookie("jwt", jwt, 60);
        }
        return res.json()["error"];
      })
      .catch((error:any) => Observable.throw(error.json().error || 'Ошибка на сервере'));
  } 

  public signinVk(): void {
    let state = "" + Math.floor(Math.random() * 100000);
    MyCookie.setCookie("oauth2_state", state, 60);
    var url = "https://oauth.vk.com/authorize?";
    url += "client_id=6635634&"; 
    url += "redirect_uri=" + encodeURI("http://jzpro.ru/server/vk") + "&"; 
    url += "display=popup&"; 
    url += "scope=email&"; 
    url += "response_type=code&"; 
    url += "v=5.80&"; 
    url += "state=" + state; 
    window.location.href = url;
  } 

  public signinOk(): void {
    let state = "" + Math.floor(Math.random() * 100000);
    MyCookie.setCookie("oauth2_state", state, 60);
    var url = "https://connect.ok.ru/oauth/authorize?";
    url += "client_id=1269238528&"; 
    url += "scope=VALUABLE_ACCESS;GET_EMAIL&"; 
    url += "response_type=code&"; 
    url += "redirect_uri=" + encodeURI("http://jzpro.ru/server/ok") + "&"; 
    url += "layout=w&"; 
    url += "state=" + state; 
    window.location.href = url;
  } 

  public getEpisodeList(query: string, offset: number, limit: number): Observable<any> {
    let queryEncoded = encodeURI(query);
    console.log("episodes01 time=" + new Date().getTime());
    return this.http.get(this.episodesUrl + offset + "/" + limit + "/" + queryEncoded, this.getOptions()).map((res:Response) => this.extractEpisodeList(res));
  } 

  private extractEpisodeList(res: Response) {
    console.log("episodes02 time=" + new Date().getTime());
    this.handleLoggedIn(res.json()["loggedin"]);
    return res.json()["episodeList"];
  }
 
  public getFavoriteEpisodeList(): Observable<any> {
    return this.http.get(this.favoritesUrl, this.getOptions()).map((res:Response) => this.extractFavoriteEpisodeList(res));
  } 

  private extractFavoriteEpisodeList(res: Response) {
    this.handleLoggedIn(res.json()["loggedin"]);
    return res.json()["episodeList"];
  }
 
  public deleteFavorite(hash: string): Observable<any> {
    return this.http.get(this.deleteFavoriteUrl + "/" + hash, this.getOptions()).map((res:Response) => this.extractDeleteFavorite(res));
  } 

  private extractDeleteFavorite(res: Response) {
    this.handleLoggedIn(res.json()["loggedin"]);
    return null;
  }
 
  public getSearchTerms(): Observable<any> {
    return this.http.get(this.searchTermsUrl, this.getOptions()).map((res:Response) => this.extractSearchTermList(res));
  } 

  private extractSearchTermList(res: Response) {
    this.handleLoggedIn(res.json()["loggedin"]);
    var values = res.json()["searchTermList"];
    for (var i = 0; i < values.length; i++) {
      values[i] = decodeURI(values[i]);
      values[i] = values[i].split("+").join(" ");
    }
    return values;
  }
 
  public deleteSearchTerm(query: string): Observable<any> {
    let queryEncoded = encodeURI(query);
    return this.http.get(this.deleteSearchTermUrl + "/" + queryEncoded, this.getOptions()).map((res:Response) => this.extractDeleteSearchTerm(res));
  } 

  private extractDeleteSearchTerm(res: Response) {
    this.handleLoggedIn(res.json()["loggedin"]);
    return null;
  }
 
  public addToFavorite(hash: string, note: string): Observable<any> {
    let noteEncoded = encodeURI(note);
    return this.http.get(this.addFavoriteUrl + "/" + hash + "/" + noteEncoded, this.getOptions()).map((res:Response) => this.extractAddFavorite(res));
  } 

  private extractAddFavorite(res: Response) {
    this.handleLoggedIn(res.json()["loggedin"]);
    return null;
  }
 
  public getEpisode(hash: string): Observable<any> {
    return this.http.get(this.episodeUrl + hash, this.getOptions()).map((res:Response) => this.extractEpisode(res));
  } 

  private extractEpisode(res: Response) {
    return res.json();
  }
 
  private handleLoggedIn(loggedin: boolean) {
    let jwt = MyCookie.getCookie("jwt");
    if (jwt && !loggedin) {
      MyCookie.deleteCookie("jwt");
      this.mainParent.onSignout();
    }
  }
 
  private getOptions() {
    let jwt = MyCookie.getCookie("jwt");
    let headers = new Headers();
    headers.append('Authorization', jwt);
    return {headers: headers};
  }

}
