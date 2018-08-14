import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';

import { Observable } from 'rxjs/Rx';
import {Subscription} from "rxjs";

import { BackendService } from '../backend.service';  

import { MyCookie } from "../cookie";

@Component({
  selector: 'app-episode',
  templateUrl: './episode.component.html',
  styleUrls: ['./episode.component.css'],
  providers: [BackendService]
})
export class EpisodeComponent implements OnInit {

  private hash: string;
  private sub: any;
  public episode: Object = null;
  private iframeStr;
  public note: string = "";

  private subscription: Subscription;

  constructor(private route: ActivatedRoute, private router: Router, private _backendService: BackendService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.hash = params['hash']; 
      this._backendService.getEpisode(this.hash).subscribe(response => {
        let episodeObj = response["episode"];
        console.log("getEpisode response=" + response.toString() + " response.episode=" + response["episode"] + " response.hash=" + response["hash"]);
        this.episode = {hash: episodeObj["hash"], title:episodeObj["title"], created:episodeObj["created"], descriptionHtml:episodeObj["descriptionHtml"], iframe:episodeObj["iframe"], note:episodeObj["note"]};

        this.iframeStr = this.sanitizer.bypassSecurityTrustHtml(this.isNotEmptyObject(this.episode) ? this.episode["iframe"] : "");

        let timer = Observable.timer(1000,1000);
        this.subscription = timer.subscribe(t => {
          if (this.isNotEmptyObject(this.episode)) {
            let iframe = this.episode["iframe"];
            if (window.innerWidth < 1024) {
              iframe = iframe.replace(/width="\d\d\d"/gi, 'width="100%"')
              iframe = iframe.replace(/height="\d\d\d"/gi, 'height="' + (window.innerWidth*0.5625) + '"')
              iframe = iframe.replace(/width="\d\d\dpx"/gi, 'width="100%"')
              iframe = iframe.replace(/height="\d\d\dpx"/gi, 'height="' + (window.innerWidth*0.5625) + '"')
              iframe = iframe.replace(/width: \d\d\dpx/gi, 'width: 100%')
              iframe = iframe.replace(/height: \d\d\dpx/gi, 'height: ' + (window.innerWidth*0.5625) + 'px')
            }
            this.iframeStr = this.sanitizer.bypassSecurityTrustHtml(this.isNotEmptyObject(this.episode) ? iframe : "");
            this.note = this.isNotEmptyObject(this.episode) ? this.episode["note"] : "";
          } else {
            this.iframeStr = this.sanitizer.bypassSecurityTrustHtml("");
          }
          this.subscription.unsubscribe();
        });
      }); 
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  public noteChanged(note: string) {
    this.note = note;
  } 

  public saveNoteClick(event) {
    if (this.note.length > 0) {
      this.episode["note"] = this.note;
      this._backendService.addToFavorite(this.episode["hash"], this.note).subscribe(
        response => {
        },
        error => {
        }
      ); 
    }
  } 

  public isNotEmptyObject(obj) {
    if (obj) {
      return true;
    } else {
      return false;
    }
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
