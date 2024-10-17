import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import {MatFormFieldModule} from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MatFormFieldModule, FormsModule, CommonModule, HttpClientModule, MatInputModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  username: string= '';
  password: string= '';
  errorMessage: string= '';

  constructor(private authService: AuthService, private router: Router){}

    onSubmit(){
    this.authService.login(this.username, this.password).subscribe(
      (response: any)=>{
        localStorage.setItem('jwtToken', response.token);
        console.log(response);
      },
      (error)=>{
        this.errorMessage= 'Invalid username or password';
        alert(this.errorMessage);
      }
    );
  }
}
