import { FormsModule } from '@angular/forms'; // Import FormsModule
import { Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { EquipmentTableComponent } from '../../shared/component-elements/equipment-table/equipment-table.component';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { EditorEquipmentComponent } from '../editor-equipment/editor-equipment.component';
import { equipmentTO } from '../../types/equipmentTO';
import { Component } from '@angular/core';

@Component({
  selector: 'app-editor-create-equipment',
  standalone: true,
  imports: [FormsModule, EquipmentTableComponent, ButtonComponent, EditorEquipmentComponent],
  templateUrl: './editor-create-equipment.component.html',
  styleUrls: ['./editor-create-equipment.component.css']
})

export class EditorCreateEquipmentComponent {
  equipment: equipmentTO = {
    equipmentNumber: "",
    name: "",
    description: "",
    type: "",
    orders: [],
  };
  createDisabled: boolean = true;
  createBtnLabel: string = 'Save Equipment';

  // Initialize equipmentState 
  equipmentState = {
    buttonIcon: 'save',
    buttonLabel: 'Save Equipment',
    isSaved: false
  };

  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService,
  ) { }

  // Method to handle Save Equipment button click with error handling
  saveEquipment(event: MouseEvent) {
    if (
      event.type === 'click' &&
      this.equipment.equipmentNumber &&
      this.equipment.name &&
      this.equipment.description &&
      this.equipment.type &&
      Array.isArray(this.equipment.orders) // Check if orders is an array
    ) {
      console.log("Payload:", this.equipment); // Debugging line to check payload
      this.backendCommunicationService.createEquipment(this.equipment)
        .pipe(
          catchError((error) => {
            console.error('Error creating equipment:', error);
            alert('Failed to create equipment. Please try again.');
            return of(null); // Return null to continue with the observable flow
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              console.log('Equipment created successfully:', response);

              // Update UI states
              this.equipmentState.buttonIcon = 'check_circle';
              this.equipmentState.buttonLabel = 'Saved';
              this.equipmentState.isSaved = true;

              alert('Equipment created successfully!');

              // Delay navigation to allow the user to see the message
              setTimeout(() => {
                this.router.navigateByUrl('/editor/equipment');
              }, 1000); // 1-second delay
            }
          },
          error: () => {
            console.error('Unexpected error occurred during equipment creation.');
          }
        });
    } else {
      console.error('All fields are required');
      alert('Please fill in all fields before saving.');
    }
  }

  // Enable the button when all fields have values
  checkFormCompletion() {
    console.log("Equipment Number:", this.equipment.equipmentNumber);
    console.log("Name:", this.equipment.name);
    console.log("Description:", this.equipment.description);
    console.log("Type:", this.equipment.type);
    console.log("Orders:", this.equipment.orders);

    this.createDisabled = !(
      this.equipment.equipmentNumber &&
      this.equipment.name &&
      this.equipment.description &&
      this.equipment.type &&
      Array.isArray(this.equipment.orders)
    );
    console.log("Create Disabled:", this.createDisabled);
  }
}