import { Component } from '@angular/core';

@Component({
  selector: 'app-dev-navigation',
  standalone: true,
  imports: [],
  templateUrl: './dev-navigation.component.html',
  styleUrl: './dev-navigation.component.css'
})
export class DevNavigationComponent {

  constructor() {
    console.log('Development Navigation Component Initialized');
  }

}
