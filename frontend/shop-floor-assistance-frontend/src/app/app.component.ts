import { Component } from '@angular/core';
import { RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { HomeComponent } from './home/home.component'; 
import { AuthService } from './service/auth.service';
import { LoginComponent } from './login/login/login.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HomeComponent, RouterLink, RouterModule, LoginComponent, HttpClientModule], 
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AuthService]
})
export class AppComponent {
  title = 'shop-floor-assistance-frontend';
  constructor(private authService: AuthService){}
}
