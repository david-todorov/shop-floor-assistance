import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { BackendCommunicationService } from './backend-communication.service';
import { userTO } from '../types/userTO';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _isLoggedIn$= new BehaviorSubject<boolean>(false);
  private readonly TOKEN_NAME= "jwt_token";
  isLoggedIn$= this._isLoggedIn$.asObservable();
  user!: userTO;

  get token(): any {
    return localStorage.getItem(this.TOKEN_NAME);
  }

  constructor(private http: HttpClient, private backendCommunicationService: BackendCommunicationService) { 
    this._isLoggedIn$.next(!!this.token);
    // this.user= this.getUserCredentials(this.token);
  }

  login(username: string, password: string): Observable<any>{
    return this.backendCommunicationService.login(username, password).pipe(
      tap((response)=>{
        this._isLoggedIn$.next(true);
        localStorage.setItem(this.TOKEN_NAME, response.token);
        this.user= this.getUserCredentials(response.token);
      })
    )
  }

  getUserCredentials(token: string): userTO {
    return JSON.parse(atob(token.split('.')[1])) as userTO;
  }



  get isLoggedIn(): boolean {
  return this._isLoggedIn$.getValue();
}

  logout(): void {
    localStorage.removeItem(this.TOKEN_NAME);
    this._isLoggedIn$.next(false);
  }
}

