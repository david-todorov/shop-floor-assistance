import { MatSidenavModule } from '@angular/material/sidenav';
import { MatCheckboxModule } from '@angular/material/checkbox';

import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { Router, RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSliderModule } from '@angular/material/slider';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';

import { HeaderComponent } from './components/header/header.component';
import { WelcomeComponent } from './components/welcome/welcome.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    MatSlideToggleModule, 
    MatSliderModule, 
    MatToolbarModule, 
    MatIconModule,
    RouterOutlet,
    RouterLink,
    HeaderComponent,
    WelcomeComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: []
})
export class AppComponent{

  title = 'Digital Workflow Assistant';
  
}
