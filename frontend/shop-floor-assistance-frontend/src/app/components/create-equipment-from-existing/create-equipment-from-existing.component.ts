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
  equipment: equipmentTO = {
    equipmentNumber: "",
    name: "",
    description: "",
    type: "",
    orders: [],
  };
  selectedEquipment: equipmentTO | null = null;
  suggestions: equipmentTO[] = []; // Store fetched suggestions
  createDisabled: boolean = true;
  createBtnLabel: string = 'Create Equipment';
  equipmentNumberExists: boolean = false; // To track uniqueness of equipment number
  
  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService,
  ) { }

  ngOnInit() {
    this.loadSuggestions();
  }

  loadSuggestions() {
    // Fetch suggestions from the backend
    this.backendCommunicationService.getEquipmentSuggestions().subscribe(
      (data) => {
        this.suggestions = data;
      },
      (error) => {
        console.error('Error fetching suggestions:', error);
      }
    );
  }

  selectSuggestion(suggestion: equipmentTO) {
    console.log("Suggestion selected:", suggestion); // Debugging line
    this.selectedEquipment = { ...suggestion, equipmentNumber: '' };
    //this.createDisabled = true; // Disable the button until the new equipment number is unique
  }

    checkUniqueEquipmentNumber() {
    if (this.selectedEquipment) {
      // Check if the equipment number exists in suggestions
      this.equipmentNumberExists = this.suggestions.some(
        (suggestion) => suggestion.equipmentNumber === this.selectedEquipment!.equipmentNumber
      );

      // Enable the button only if all fields are filled and the equipment number is unique
      this.createDisabled = this.equipmentNumberExists || !(
        this.selectedEquipment.equipmentNumber &&
        this.selectedEquipment.name &&
        this.selectedEquipment.description &&
        this.selectedEquipment.type
      );
    }
  }

  createEquipment(event: MouseEvent) {
    if (event.type === 'click' && !this.equipmentNumberExists) {
      console.log("Payload being sent:", this.selectedEquipment);
      this.backendCommunicationService.createEquipment(this.selectedEquipment)
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
      console.error('Equipment number must be unique');
      alert('Please enter a unique equipment number.');
    }
  }

}


