import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, inject, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { loginState } from '../../shared/types/login-state';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { tap } from 'rxjs';
import { Router } from '@angular/router';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';

@Component({
  selector: 'app-login-register',
  standalone: true,
  imports: [
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    ButtonComponent
  ],
  templateUrl: './login-register.component.html',
  styleUrl: './login-register.component.css'
})
  /**
   * Implementation of login component.
   * Registration is not currently implemented (as per customer specification)
   * @author Jossin Antony (contributor)
   */
export class LoginRegisterComponent implements OnInit, OnDestroy{

  /**
   * Parameters of login component
   */
  btnLabel = "Log In"

  loginUIState!: loginState;
  form !: FormGroup<any>;
  disabledd: boolean= false;

  /**
   * Inject the rerquired services
   * Initialize log in form
   */
  constructor(
    private backendCommunicationService: BackendCommunicationService,
    private router : Router,
    private fb: FormBuilder){
      this.form = this.fb.group({
        username: ['', Validators.required],
        password: ['', Validators.required]
      });
    }

  /**
   * Initialization of login component.
   * If the user is already loggd in, redirect to editor or operator homepage based on assigned role.
   * i.e, an already logged in user can not reach the login page again unless he logs out first
   * 
   * If the user is already not logged in, initialize the login state from the login service.
   */
  ngOnInit(): void {
    this.loginUIState= this.backendCommunicationService.getloginUIState();
    if(this.loginUIState && this.loginUIState.isLoggedIn){
      switch(this.loginUIState.currentRole){
        case 'editor':
          this.router.navigateByUrl('/editor-homepage');
          break;
        case 'operator':
          this.router.navigateByUrl('/operator/orders');
          break;
      }
    }else{      
      this.backendCommunicationService.loginUIState$.subscribe(
        (state) => {
          this.loginUIState = state;
        },
        ()=>{
           this.loginUIState= this.backendCommunicationService.getloginUIState();
        }
      );
    }
  }

  /**
   * On destruction of the component:
    * set the login visibility  and login state corresponding to successful/unsuccessful login
    * Boradcast the login states via the defined observable 
   */
  ngOnDestroy(): void {
    this.loginUIState.isLoginVisible= true;
    this.backendCommunicationService._loginUIState$.next(this.loginUIState);
    this.backendCommunicationService.setLoginStates(this.loginUIState);
  }

  /**
   * Set focus to the username input field as a visual aid to prompt user to log in
   */
  @ViewChild('uname') uname!: ElementRef;
  ngAfterViewInit() {
    this.uname.nativeElement.focus(); 
  }

  /**
   * Navigation based on user role
   */
  loadUserPage(userRole: string) {
    if(userRole === 'editor'){
      this.router.navigateByUrl('/editor-homepage');
    }else if(userRole === 'operator'){
      this.router.navigateByUrl('/operator/orders');
    }
  }

  /**
   * 'Log In' button action
    * Retrieve the entered information and  pass it to backend to generate the proper authentication tokens.
    * Set proper values of the login states.
   */
  resolveButtonClick(event: MouseEvent) {
    if(event.type==='click' && this.form.valid){
      const {username, password}= this.form.value;
      return this.backendCommunicationService.login(username, password).subscribe(
        (response)=>{
          const credentials= this.backendCommunicationService.getUserCredentials(response.token);
          this.loginUIState.buttonIcon='logout'
          this.loginUIState.buttonLabel='Log Out'
          this.loginUIState.isLoggedIn= true
          this.loginUIState.currentRole= credentials.sub
          this.loginUIState.rolesAvailable= credentials.roles
          this.loginUIState.jwtToken= response.token
          
          this.backendCommunicationService.setLoginStates(this.loginUIState);
          this.loadUserPage(credentials.sub);
        },
        ()=>{
          alert('Incorrect user credentials.')
          //Todo: Handle errors gracefully
        }
      );
    }
    return;
  }

}

