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

  createFromScratch() {
    // Navigate to the "Create from Scratch" component
    this.router.navigateByUrl('/editor-orders/create');
  }

  createFromExisting() {
    // Navigate to the "Create from Existing" component
    this.router.navigateByUrl('/editor-orders/create-from-existing');
  }
}


