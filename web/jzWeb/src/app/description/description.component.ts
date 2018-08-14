import { Component, OnInit, Input, ChangeDetectorRef } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

import { Observable } from 'rxjs/Rx';
import {Subscription} from "rxjs";

import { MainComponent } from '../main/main.component'; 

import { BackendService } from '../backend.service';  

import { MyCookie } from "../cookie";

@Component({
  selector: 'app-description',
  templateUrl: './description.component.html',
  styleUrls: ['./description.component.css']
})
export class DescriptionComponent implements OnInit {

  @Input() parent: MainComponent; 
  private _episode: Object;
  private iframeStr;
  public note: string = "";
  public isSignedIn: boolean;

  private subscription: Subscription;

  constructor(private sanitizer: DomSanitizer, private cdRef:ChangeDetectorRef, private _backendService: BackendService) { }

  ngOnInit() {
    this.calcIsSigninIn();
  }

  @Input("episode") set episode(value: Object) {
    this._episode = value;
    console.log("hashDescription1: " + (this.isNotEmptyObject(this._episode) ? this._episode["hash"] : ""));
    this.iframeStr = "";
    this.note = "";

    let timer = Observable.timer(1000,1000);
    this.subscription = timer.subscribe(t => {
      this.iframeStr = this.sanitizer.bypassSecurityTrustHtml(this.isNotEmptyObject(this._episode) ? this._episode["iframe"] : "");
      this.note = this.isNotEmptyObject(this._episode) ? this._episode["note"] : "";
      this.subscription.unsubscribe();
    });
  }

  get episode(): Object {
    return this._episode;
  }

  public calcIsSigninIn() {
    let jwt = MyCookie.getCookie("jwt");
    if (jwt) {
      this.isSignedIn = true;
    } else {
      this.isSignedIn = false;
    }
  }

  public isNotEmptyObject(obj) {
    if (obj) {
      return true;
    } else {
      return false;
    }
  }

  public noteChanged(note: string) {
    this.note = note;
  } 

  public saveNoteClick(event) {
    console.log("saveNoteClick note=" + this.note);
    if (this.note.length > 0) {
      this._episode["note"] = this.note;
      this._backendService.addToFavorite(this._episode["hash"], this.note).subscribe(
        response => {
          console.log("saveNoteClick onResponse");
          this.parent.onFavoriteChanged();
        },
        error => {
        }
      ); 
    }
  } 

}
