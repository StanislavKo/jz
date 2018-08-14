import { Component, OnInit, Input } from '@angular/core';

import { MainComponent } from '../main/main.component'; 

import { BackendService } from '../backend.service';  

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {

  @Input() parent: MainComponent;
  private username: string = "";
  private password1: string = "";
  public loginError: string = "";

  constructor(private _backendService: BackendService) { }

  ngOnInit() {
  }

  public usernameChanged(username: string) {
    this.username = username;
  } 

  public password1Changed(password1: string) {
    this.password1 = password1;
  } 

  public signinClick(event) {
    if (this.username.length == 0) {
      this.loginError = "Введите логин";
      return;
    }
    if (this.password1.length == 0) {
      this.loginError = "Введите пароль";
      return;
    }
    this.loginError = "";

    this._backendService.signin(this.username, this.password1).subscribe(
      response => {
        this.parent.onSignin();
      },
      error => {
        this.loginError = error == "Unauthorized" ? "Ошибка в логине или пароле" : error;
      }
    ); 
  } 

  public doVk() {
    this._backendService.signinVk(); 
  }

  public doOk() {
    this._backendService.signinOk(); 
  }

}
