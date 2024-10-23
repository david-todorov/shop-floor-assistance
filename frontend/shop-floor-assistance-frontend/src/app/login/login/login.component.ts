import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import {MatFormFieldModule} from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { userTO } from '../../types/userTO';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MatFormFieldModule, FormsModule, CommonModule, HttpClientModule, MatInputModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{

  username: string= '';
  password: string= '';
  errorMessage: string= '';

  constructor(private authService: AuthService, private router: Router, private http: HttpClient){}
  
  ngOnInit(): void {
      if (this.authService.isLoggedIn) {
      this.authService.logout();
      console.log('logedout');
      //easyhack, to be refactored 
      // this.router.navigate(['/login'], { replaceUrl: true }).then(() => {
      //   history.pushState(null, '', '/login');
      //   history.pushState(null, '', '/login');
      //   history.go(-1);
      // });
    }
  }

    onSubmit(){
    this.authService.login(this.username, this.password).subscribe(
      (response: any)=>{
        const token: any = localStorage.getItem("jwt_token")?.split('.')[1];
        const user: any = JSON.parse(atob(token)) as userTO;
        console.log(user)
        // this.router.navigate(['/home']);
        if (user.roles.includes('ROLE_OPERATOR')) {
          this.router.navigate(['operator']);
        } else if (user.roles.includes('ROLE_EDITOR')) {
          this.router.navigate(['editor']);
        } else {
          alert('Unauthorized role');
        }

      },
      (error)=>{
        this.errorMessage= 'Invalid username or password';
        alert(this.errorMessage);
      },
      ()=>{
        this.errorMessage='';
      }
    );
  }
}
