import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonComponent } from "../../shared/component-elements/button/button.component";

@Component({
  selector: 'app-product-creation-option',
  standalone: true,
  imports: [ButtonComponent],
  templateUrl: './product-creation-option.component.html',
  styleUrl: './product-creation-option.component.css'
})
export class ProductCreationOptionComponent {
  constructor(private router: Router) {}

  // Labels for the buttons
  createBtnLabel: string = 'From template';
  createNewBtnLabel: string = 'Create new';

  // Method to navigate to the "Create from Scratch" component
  createFromScratch() {
    this.router.navigateByUrl('/editor-product/create');
  }

  // Method to navigate to the "Create from Existing" component
  createFromExisting() {
    this.router.navigateByUrl('/editor-product/create-from-existing');
  }
}
