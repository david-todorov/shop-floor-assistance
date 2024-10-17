import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  private loginURL: string= 'http://localhost:8080/auth/login'

  login(username: string, password: string): Observable<any>{
    console.log(`post request url: ${this.loginURL}, username: ${username}, password: ${password}`)
    return this.http.post(this.loginURL,{username, password});
  }
}

