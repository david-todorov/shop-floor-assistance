import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { userRoleTO, userTO } from '../types/userTO';
import { loginUIState } from '../shared/component-elements/login-state';

@Injectable({
  providedIn: 'root'
})
export class BackendCommunicationService {

  public _loginUIState$ = new BehaviorSubject<loginUIState>(null as unknown as loginUIState);
  public loginUIState$= this._loginUIState$.asObservable();
  public LOGIN_UI_SAVED_STATE= "login_ui_saved_state";
  public loginUIState: loginUIState= {
    isLoginVisible: true,
    buttonLabel: 'Start',
    buttonIcon: 'restart_alt'
  };

  constructor() { 
    const loginUISavedState: loginUIState | null = this.getloginUIState();
    if(loginUISavedState != null){
      this.loginUIState=loginUISavedState;
    }
    this.setLoginStates(this.loginUIState);
  }

  // private apiServerURL: string= environment.baseUrl;
  // private readonly TOKEN_NAME= "jwt_token";

  // _isLoggedIn$ = new BehaviorSubject<boolean>(false);
  // _userRole$ = new BehaviorSubject<userRoleTO | null>(null);

  // login(username: string, password: string): Observable<any>{
  //   return this.http.post(`${this.apiServerURL}auth/login`,{username, password});
  // }

  //---------------------------------------------------
  //Managing loginUIStates

  setLoginStates(loginUIState: loginUIState){
    this.loginUIState = loginUIState;
    this._loginUIState$.next(this.loginUIState);
    sessionStorage.setItem(this.LOGIN_UI_SAVED_STATE, JSON.stringify(this.loginUIState));
    console.log('setLoginStates', this.loginUIState)
  }

  public getloginUIState(): loginUIState {
    const data = sessionStorage.getItem(this.LOGIN_UI_SAVED_STATE);
    console.log(' getloginUIState', this.loginUIState)
    return data ? JSON.parse(data) : null;
  }
}
