import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonComponent } from "../../shared/component-elements/button/button.component";

@Component({
  selector: 'app-equipment-creation-option',
  standalone: true,
  imports: [ButtonComponent],
  templateUrl: './equipment-creation-option.component.html',
  styleUrl: './equipment-creation-option.component.css'
})
export class EquipmentCreationOptionComponent {
  constructor(private router: Router) {}

  // Labels for the buttons
  createBtnLabel: string = 'From template';
  createNewBtnLabel: string = 'Create new';

  // Method to navigate to the "Create from Scratch" component
  createFromScratch() {
    this.router.navigateByUrl('/editor-equipment/create');
  }

  // Method to navigate to the "Create from Existing" component
  createFromExisting() {
    this.router.navigateByUrl('/editor-equipment/create-from-existing');
  }

}
