import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { userRoleTO, userTO } from '../types/userTO';
import { loginState } from '../shared/component-elements/login-state';

@Injectable({
  providedIn: 'root'
})
export class BackendCommunicationService {

  public _loginUIState$ = new BehaviorSubject<loginState>(null as unknown as loginState);
  public loginUIState$= this._loginUIState$.asObservable();
  public LOGIN_UI_SAVED_STATE= "login_ui_saved_state";
  public loginUIState: loginState= {
    isLoginVisible: true,
    isLoggedIn: false,
    buttonLabel: 'Start',
    buttonIcon: 'restart_alt',
    rolesAvailable: [null],
    currentRole: null,
    jwtToken: ''
  };
  

  constructor(private http: HttpClient) { 
    const loginUISavedState: loginState | null = this.getloginUIState();
    if(loginUISavedState != null){
      this.loginUIState=loginUISavedState;
    }
    this.setLoginStates(this.loginUIState);
  }

  private apiServerURL: string= environment.baseUrl;
  // private readonly TOKEN_NAME= "jwt_token";

  // _isLoggedIn$ = new BehaviorSubject<boolean>(false);
  // _userRole$ = new BehaviorSubject<userRoleTO | null>(null);

  login(username: string, password: string): Observable<any>{
    console.log('in login', username, password)
    return this.http.post(`${this.apiServerURL}auth/login`,{username, password});
  }

  getUserCredentials(token: string): userTO {
    return JSON.parse(atob(token.split('.')[1])) as userTO;
  }
  //---------------------------------------------------
  //Managing loginUIStates

  setLoginStates(loginUIState: loginState){
    this.loginUIState = loginUIState;
    this._loginUIState$.next(this.loginUIState);
    sessionStorage.setItem(this.LOGIN_UI_SAVED_STATE, JSON.stringify(this.loginUIState));
  }

  public getloginUIState(): loginState {
    const data = sessionStorage.getItem(this.LOGIN_UI_SAVED_STATE);
    return data ? JSON.parse(data) : null;
  }
}
