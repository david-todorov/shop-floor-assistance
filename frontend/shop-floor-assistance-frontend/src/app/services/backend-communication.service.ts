import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { userRoleTO, userTO } from '../types/userTO';

@Injectable({
  providedIn: 'root'
})
export class BackendCommunicationService {
  constructor(private http: HttpClient) { }

  private apiServerURL: string= environment.baseUrl;
  private readonly TOKEN_NAME= "jwt_token";

  _isLoggedIn$ = new BehaviorSubject<boolean>(false);
  _userRole$ = new BehaviorSubject<userRoleTO | null>(null);

  login(username: string, password: string): Observable<any>{
    return this.http.post(`${this.apiServerURL}auth/login`,{username, password});
  }

  
}
