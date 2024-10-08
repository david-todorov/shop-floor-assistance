import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from './home/home.component'; 

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HomeComponent], 
  template: '<app-home></app-home>', 
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'shop-floor-assistance-frontend';
}
