import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, inject, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { loginState } from '../../shared/component-elements/login-state';
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
export class LoginRegisterComponent implements OnInit, OnDestroy{

  btnLabel = "Log In"

  loginUIState!: loginState;
  form !: FormGroup<any>;

  constructor(
    private backendCommunicationService: BackendCommunicationService,
    private router : Router,
    private fb: FormBuilder){
      this.form = this.fb.group({
        username: ['', Validators.required],
        password: ['', Validators.required]
      });
    }

  ngOnInit(): void {
    this.backendCommunicationService.loginUIState$.subscribe(
      (state) => {
        this.loginUIState = state;
      },
      ()=>{
         this.loginUIState= this.backendCommunicationService.getloginUIState();
      }
    );
  }

  ngOnDestroy(): void {
    this.loginUIState.isLoginVisible= true;
    this.backendCommunicationService._loginUIState$.next(this.loginUIState);
    this.backendCommunicationService.setLoginStates(this.loginUIState);
  }

  @ViewChild('uname') uname!: ElementRef;
  ngAfterViewInit() {
    this.uname.nativeElement.focus(); // Sets focus to the input field
  }

  loadUserPage(userRole: string) {
    if(userRole === 'editor'){
      this.router.navigateByUrl('/editor');
    }else if(userRole === 'operator'){
      this.router.navigateByUrl('/operator');
    }
  }

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
          }
        );
    }
      return;
  }

}

