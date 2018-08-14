import { Component, OnInit, Input } from '@angular/core';

import { MainComponent } from '../main/main.component'; 

import { BackendService } from '../backend.service';  

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  @Input() parent: MainComponent;
  private username: string = "";
  private password1: string = "";
  private password2: string = "";
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

  public password2Changed(password2: string) {
    this.password2 = password2;
  } 

  public singupClick(event) {
    if (this.username.length == 0) {
      this.loginError = "Введите логин";
      return;
    }
    if (this.password1.length == 0) {
      this.loginError = "Введите пароль";
      return;
    }
    if (this.password1 != this.password2) {
      this.loginError = "Пароли не совпадают";
      return;
    }
    this.loginError = "";

    this._backendService.signup(this.username, this.password1).subscribe(
      response => {
        if (response) {
          this.loginError = response == "busy" ? "Логин занят" : response;
        } else {
          this.parent.onSignin();
        }
      },
      error => {
        this.loginError = error;
      }
    ); 
  } 

}
