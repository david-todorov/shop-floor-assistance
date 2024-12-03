import { FormsModule } from '@angular/forms'; 
import { Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { equipmentTO } from '../../shared/types/equipmentTO';
import { Component } from '@angular/core';

@Component({
  selector: 'app-editor-create-equipment',
  standalone: true,
  imports: [FormsModule, ButtonComponent],
  templateUrl: './editor-create-equipment.component.html',
  styleUrls: ['./editor-create-equipment.component.css']
})

export class EditorCreateEquipmentComponent {
  // Initializing equipment object with default values
  equipment: equipmentTO = {
    equipmentNumber: "",
    name: "",
    description: "",
    type: "",
    orders: [],
  };

  // Button state to control if the Save button is disabled
  createDisabled: boolean = true;
  // Label for the Save button
  createBtnLabel: string = 'Save Equipment';

  // Object for managing UI states for the equipment save button
  equipmentState = {
    buttonIcon: 'save', // Default icon for the button
    buttonLabel: 'Save Equipment', // Default label for the button
    isSaved: false // State flag indicating if the equipment is saved
  };

  constructor(
    private router: Router, // Injecting the Router for navigation
    private backendCommunicationService: BackendCommunicationService, // Injecting backend service for API calls
  ) { }

  // Method to handle Save Equipment button click with error handling
  saveEquipment(event: MouseEvent) {
    if (
      event.type === 'click' && // Ensure the event is a click
      this.equipment.equipmentNumber && // Check that all required fields are filled
      this.equipment.name &&
      this.equipment.description &&
      this.equipment.type &&
      Array.isArray(this.equipment.orders) // Check if orders is an array
    ) {
      // Call backend service to create the equipment
      this.backendCommunicationService.createEquipment(this.equipment)
        .pipe(
          catchError((error) => {
            alert('Failed to create equipment. Please try again.'); // Handle error
            return of(null); // Return null to continue observable flow
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              // Update UI states after successful creation
              this.equipmentState.buttonIcon = 'check_circle'; // Update button icon
              this.equipmentState.buttonLabel = 'Saved'; // Update button label
              this.equipmentState.isSaved = true; // Mark equipment as saved

              alert('Equipment created successfully!'); // Notify user of success

              // Delay navigation to allow user to see the success message
              setTimeout(() => {
                this.router.navigateByUrl('/editor/equipment'); // Redirect to equipment list page
              }, 1000); // 1-second delay
            }
          },
          error: () => {
            alert('Unexpected error occurred during equipment creation.'); // Handle unexpected error
          }
        });
    } else {
      alert('Please fill in all fields before saving.'); // Alert if form is incomplete
    }
  }

  // Enable the Save button when all form fields have values
  checkFormCompletion() {
    this.createDisabled = !(
      this.equipment.equipmentNumber && // Ensure all fields are filled
      this.equipment.name &&
      this.equipment.description &&
      this.equipment.type &&
      Array.isArray(this.equipment.orders) // Ensure orders is an array
    );
  }
}
