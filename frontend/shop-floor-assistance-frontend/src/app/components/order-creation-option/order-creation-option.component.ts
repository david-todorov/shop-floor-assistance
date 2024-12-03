import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonComponent } from "../../shared/component-elements/button/button.component";

@Component({
  selector: 'app-order-creation-option',
  standalone: true,
  imports: [ButtonComponent],
  templateUrl: './order-creation-option.component.html',
  styleUrl: './order-creation-option.component.css'
})
export class OrderCreationOptionComponent {
  constructor(private router: Router) {}

  // Labels for the buttons
  createBtnLabel: string = 'From template';
  createNewBtnLabel: string = 'Create new';

  // Method to navigate to "Create from Scratch" component
  createFromScratch() {
    this.router.navigateByUrl('/editor-orders/create');
  }

  // Method to navigate to the "Create from Existing" component
  createFromExisting() {
    this.router.navigateByUrl('/editor-orders/create-from-existing');
  }
}


