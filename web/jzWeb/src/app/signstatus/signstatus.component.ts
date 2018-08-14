import { Component, OnInit, Input } from '@angular/core';

import { MainComponent } from '../main/main.component';

import { MyCookie } from "../cookie";
import { MyJwt } from "../jwt";

@Component({
  selector: 'app-signstatus',
  templateUrl: './signstatus.component.html',
  styleUrls: ['./signstatus.component.css']
})
export class SignstatusComponent implements OnInit {

  @Input() parent: MainComponent;
  public isSignedIn: boolean;
  public username: string;

  constructor() { }

  ngOnInit() {
    this.calcIsSigninIn();
  }

  public calcIsSigninIn() {
    let jwt = MyCookie.getCookie("jwt");
    if (jwt) {
      this.isSignedIn = true;
      this.username = MyJwt.getUsernameFromJwt(jwt);
      console.log("calcIsSigninIn=" + this.username);
    } else {
      this.isSignedIn = false;
    }
  }

  public signin() {
    this.parent.signin();
  }

  public signout() {
    MyCookie.deleteCookie("jwt");
    this.parent.onSignout();
  }

}
