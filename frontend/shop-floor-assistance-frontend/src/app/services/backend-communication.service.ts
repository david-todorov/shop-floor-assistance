import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http'; // Importing Angular HTTP client
import { Injectable } from '@angular/core'; // Injectable decorator for service

import { BehaviorSubject, Observable } from 'rxjs'; // Reactive programming utilities
import { environment } from '../../environments/environment'; // Environment configuration
import { userRoleTO, userTO } from '../shared/types/userTO'; // User-related types
import { loginState } from '../shared/types/login-state'; // Login state type
import { productTO } from '../shared/types/productTO'; // Product-related types
import { OperatorExecutionTO } from '../shared/types/OperatorExecutionTO'; // Operator execution type

@Injectable({
  providedIn: 'root' // Service available throughout the app
})
 /**
   * Backend communication service
   * 
   * This file contains all api endpoints that are being utilized in the frontend of the application.
   * @author Jossin Antony (contributor)
   */
export class BackendCommunicationService {
  
  // Observable and BehaviorSubject for login UI state management
  public _loginUIState$ = new BehaviorSubject<loginState>(null as unknown as loginState);
  public loginUIState$ = this._loginUIState$.asObservable();

  // Local storage key for login state
  public LOGIN_UI_SAVED_STATE = "login_ui_saved_state";

  // Default login UI state
  public loginUIState: loginState = {
    isLoginVisible: true,
    isLoggedIn: false,
    buttonLabel: 'Start',
    buttonIcon: 'restart_alt',
    rolesAvailable: [null],
    currentRole: null,
    jwtToken: ''
  };

  // Base API URL from environment
  private apiServerURL: string = environment.baseUrl;

  constructor(private http: HttpClient) {
    // Load saved login UI state from session storage if available
    const loginUISavedState: loginState | null = this.getloginUIState();
    if (loginUISavedState != null) {
      this.loginUIState = loginUISavedState;
    }
    this.setLoginStates(this.loginUIState);
  }

  // Method for user login
  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiServerURL}auth/login`, { username, password });
  }

  // Decode JWT to extract user credentials
  getUserCredentials(token: string): userTO {
    return JSON.parse(atob(token.split('.')[1])) as userTO;
  }

  // ------------------------------------
  // Managing login UI states
  setLoginStates(loginUIState: loginState) {
    this.loginUIState = loginUIState;
    this._loginUIState$.next(this.loginUIState);
    sessionStorage.setItem(this.LOGIN_UI_SAVED_STATE, JSON.stringify(this.loginUIState));
  }

  getloginUIState(): loginState {
    const data = sessionStorage.getItem(this.LOGIN_UI_SAVED_STATE);
    return data ? JSON.parse(data) : null;
  }

  // ------------------------------------
  // API calls for operator functionality
  getAllOrders(): Observable<any> {
    return this.http.get(`${this.apiServerURL}operator/orders`);
  }

  getOperatorOrder(id: number): Observable<any> {
    return this.http.get(`${this.apiServerURL}operator/orders/${id}`);
  }

  startOrder(id: number, orderData: any): Observable<OperatorExecutionTO> {
    return this.http.post<OperatorExecutionTO>(`${this.apiServerURL}operator/start/${id}`, orderData);
  }

  finishOrder(id: number): Observable<OperatorExecutionTO> {
    return this.http.put<OperatorExecutionTO>(`${this.apiServerURL}operator/finish/${id}`, {});
  }

  getForecast(id: number): Observable<{ totalTimeRequired: number | null }> {
    return this.http.get<{ totalTimeRequired: number | null }>(`${this.apiServerURL}operator/forecast/${id}`);
  }

  abortOrderid(id: number): Observable<OperatorExecutionTO> {
    return this.http.put<OperatorExecutionTO>(`${this.apiServerURL}operator/abort/${id}`, {});
  }

  // ------------------------------------
  // API calls for orders management
  getEditorOrders(): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/orders`);
  }

  getEditorOrder(id: number): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/orders/${id}`);
  }

  createOrder(orderData: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiServerURL}editor/orders`, JSON.stringify(orderData), { headers });
  }

  updateEditorOrder(id: number, orderData: any): Observable<any> {
    return this.http.put(`${this.apiServerURL}editor/orders/${id}`, orderData);
  }

  deleteEditorOrder(id: number): Observable<any> {
    return this.http.delete(`${this.apiServerURL}editor/orders/${id}`);
  }

  // ------------------------------------
  // API calls for equipment management
  getAllEditorEquipment(): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/equipment`);
  }

  getEditorEquipment(id: number): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/equipment/${id}`);
  }

  createEquipment(equipmentData: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiServerURL}editor/equipment`, JSON.stringify(equipmentData), { headers });
  }

  getEquipmentSuggestions(): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/equipment/suggestions`);
  }

  updateEditorEquipment(id: number, equipmentData: any): Observable<any> {
    return this.http.put(`${this.apiServerURL}editor/equipment/${id}`, equipmentData);
  }

  deleteEditorEquipment(id: number): Observable<any> {
    return this.http.delete(`${this.apiServerURL}editor/equipment/${id}`);
  }

  // ------------------------------------
  // API calls for product management
  getAllEditorProduct(): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/products`);
  }

  getEditorProduct(id: number): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/products/${id}`);
  }

  createProduct(productData: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiServerURL}editor/products`, JSON.stringify(productData), { headers });
  }

  getProductSuggestions(): Observable<any> {
    return this.http.get(`${this.apiServerURL}editor/products/suggestions`);
  }

  updateEditorProduct(id: number, productData: any): Observable<any> {
    return this.http.put(`${this.apiServerURL}editor/products/${id}`, productData);
  }

  deleteEditorProduct(id: number): Observable<any> {
    return this.http.delete(`${this.apiServerURL}editor/products/${id}`);
  }

  // ------------------------------------
  // API calls for getting suggestions from previously executed orders and workflows
  getWorkflowSuggestions(productAfter: productTO): Observable<HttpResponse<any>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<HttpResponse<any>>(`${this.apiServerURL}editor/workflows/suggestions`, productAfter, { headers, observe: 'response' });
  }

  getTaskSuggestions(productAfter: productTO): Observable<HttpResponse<any>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<HttpResponse<any>>(`${this.apiServerURL}editor/tasks/suggestions`, productAfter, { headers, observe: 'response' });
  }

  getItemSuggestions(productAfter: productTO): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<HttpResponse<any>>(`${this.apiServerURL}editor/items/suggestions`, productAfter, { headers, observe: 'response' });
  }
}
