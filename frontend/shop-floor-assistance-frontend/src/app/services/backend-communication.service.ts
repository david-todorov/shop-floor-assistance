import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { userRoleTO, userTO } from '../types/userTO';
import { loginState } from '../shared/component-elements/login-state';
// import { productTO } from '../types/productTO';

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

  login(username: string, password: string): Observable<any>{
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
//--------------------------------------
//api calls
  getOperatorOrders(): Observable<any>{
    return this.http.get(`${this.apiServerURL}operator/orders`);
  }

  getOperatorOrder(id: string): Observable<any>{
    return this.http.get(`${this.apiServerURL}operator/orders/${id}`);
  }

  getEditorOrders(): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/orders`);
  }

  getEditorOrder(id: string): Observable<any>{
    return this.http.get(`${this.apiServerURL}editor/orders/${id}`);
  }

  getAllEditorEquipment(): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/equipment`);
  }

  getEditorEquipment(id: number): Observable<any>{
    return this.http.get(`${this.apiServerURL}editor/equipment/${id}`);
  }

  createEquipment(equipmentData: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiServerURL}editor/equipment`, JSON.stringify(equipmentData), {headers});
  }

  updateEditorEquipment(id: number, equipmentData: any): Observable<any> {
  return this.http.put(`${this.apiServerURL}editor/equipment/${id}`, equipmentData);
}

deleteEditorEquipment(id: number): Observable<any>{
    return this.http.delete(`${this.apiServerURL}editor/equipment/${id}`);
  }

 getAllEditorProduct(): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/products`);
  }

   getEditorProduct(id: number): Observable<any>{
    return this.http.get(`${this.apiServerURL}editor/products/${id}`);
  }

  createProduct(productData: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiServerURL}editor/products`, JSON.stringify(productData), {headers});
  
  }

  updateEditorProduct(id: number, productData: any): Observable<any>  {
    return this.http.put(`${this.apiServerURL}editor/products/${id}`, productData);
  }

  deleteEditorProduct(id: number): Observable<any>{
    return this.http.delete(`${this.apiServerURL}editor/products/${id}`);
  }

}
