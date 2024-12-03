import { Component, OnInit } from '@angular/core';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { catchError, of } from 'rxjs';
import { Router } from '@angular/router';
import { equipmentTO } from '../../shared/types/equipmentTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';

@Component({
  selector: 'app-create-equipment-from-existing',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonComponent],
  templateUrl: './create-equipment-from-existing.component.html',
  styleUrl: './create-equipment-from-existing.component.css'
})
export class CreateEquipmentFromExistingComponent {

  // Initialize the equipment object with default values
  equipment: equipmentTO = {
    equipmentNumber: "",
    name: "",
    description: "",
    type: "",
    orders: [],
  };

  // Variable to hold the selected equipment
  selectedEquipment: equipmentTO | null = null;
  // Store fetched equipment suggestions
  suggestions: equipmentTO[] = [];
  // Flag to enable/disable the Create button
  createDisabled: boolean = true;
  // Button label for creating equipment
  createBtnLabel: string = 'Create Equipment';
  // Flag to track uniqueness of the equipment number
  equipmentNumberExists: boolean = false;

  constructor(
    private router: Router, // Router for navigation
    private backendCommunicationService: BackendCommunicationService, // Backend service to interact with the API
  ) { }

  // Fetch the equipment suggestions from the backend when the component initializes
  ngOnInit() {
    this.loadSuggestions(); // Load equipment suggestions
  }

  // Fetch equipment suggestions from the backend
  loadSuggestions() {
    this.backendCommunicationService.getEquipmentSuggestions().subscribe(
      (data) => {
        this.suggestions = data; // Populate the suggestions with fetched data
      },
      (error) => {
        alert('Error fetching equipment suggestions');
      }
    );
  }

  // Handle the selection of an equipment suggestion and reset the equipment number
  selectSuggestion(suggestion: equipmentTO) {
    this.selectedEquipment = { ...suggestion, equipmentNumber: '' }; // Copy selected suggestion and reset equipment number
  }

  // Check if the selected equipment number is unique
  checkUniqueEquipmentNumber() {
    if (this.selectedEquipment) {
      // Check if the equipment number already exists in the suggestions
      this.equipmentNumberExists = this.suggestions.some(
        (suggestion) => suggestion.equipmentNumber === this.selectedEquipment!.equipmentNumber
      );

      // Enable the Create button only if the equipment number is unique and all fields are filled
      this.createDisabled = this.equipmentNumberExists || !(
        this.selectedEquipment.equipmentNumber &&
        this.selectedEquipment.name &&
        this.selectedEquipment.description &&
        this.selectedEquipment.type
      );
    }
  }

  // Handle the creation of equipment
  createEquipment(event: MouseEvent) {
    if (event.type === 'click' && !this.equipmentNumberExists) {
      // Call the backend service to create the equipment
      this.backendCommunicationService.createEquipment(this.selectedEquipment!)
        .pipe(
          catchError((error) => {
            alert('Failed to create equipment. Please try again.');
            return of(null); // Continue with observable flow after error
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              alert('Equipment created successfully!');

              // Delay navigation to allow the user to see the success message
              setTimeout(() => {
                this.router.navigateByUrl('/editor/equipment'); // Redirect to the equipment page
              }, 1000); // 1-second delay
            }
          },
          error: () => {
            alert('Unexpected error occurred during equipment creation.');
          }
        });
    } else {
      alert('Please enter a unique equipment number.'); // Alert if the equipment number is not unique
    }
  }
}


