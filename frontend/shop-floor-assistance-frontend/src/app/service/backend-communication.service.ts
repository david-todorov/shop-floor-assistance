import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BackendCommunicationService {

  constructor(private http: HttpClient) { }

  private loginURL: string= `${environment .baseUrl}auth/login`;

  login(username: string, password: string): Observable<any>{
    return this.http.post(this.loginURL,{username, password});
  }
}
