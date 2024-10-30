import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, inject, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { loginUIState } from '../../shared/component-elements/login-state';
import { BackendCommunicationService } from '../../services/backend-communication.service';

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
  ],
  templateUrl: './login-register.component.html',
  styleUrl: './login-register.component.css'
})
export class LoginRegisterComponent implements OnInit, OnDestroy{

  constructor(private backendCommunicationService: BackendCommunicationService){
    console.log('in login constructor')

  }
  ngOnDestroy(): void {
    this.loginUiState.isLoginVisible= true;
    this.backendCommunicationService._loginUIState$.next(this.loginUiState);
    this.backendCommunicationService.setLoginStates(this.loginUiState);
    console.log('in destroy of login component',this.loginUiState);

  }

  @ViewChild('uname') uname!: ElementRef;
  ngAfterViewInit() {
    this.uname.nativeElement.focus(); // Sets focus to the input field
  }

    form: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });

  submit() {
    if (this.form.valid) {
      this.submitEM.emit(this.form.value);
    }
  }
  @Input() error!: string | null;

  @Output() submitEM = new EventEmitter();

//----------------------------------------
  loginButtonVisible = true;
  loginUiState!: loginUIState;

  ngOnInit(): void {
    this.backendCommunicationService.loginUIState$.subscribe(
      (state) => {
        this.loginUiState = state;
      },
      ()=>{
         this.loginUiState= this.backendCommunicationService.getloginUIState();
      }
    );
  }
}
