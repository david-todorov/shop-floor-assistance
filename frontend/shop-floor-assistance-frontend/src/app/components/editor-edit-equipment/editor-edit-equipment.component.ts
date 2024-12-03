import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { equipmentTO } from '../../shared/types/equipmentTO';
import { catchError, of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';

@Component({
  selector: 'app-editor-edit-equipment',
  standalone: true,
  imports: [FormsModule, ButtonComponent],
  templateUrl: './editor-edit-equipment.component.html',
  styleUrl: './editor-edit-equipment.component.css'
})
export class EditorEditEquipmentComponent {
  // Initializing equipment object with default values
  equipment: equipmentTO = {
    equipmentNumber: "",
    name: "",
    description: "",
    type: "",
    orders: [],
  };

  // Numeric ID of the equipment (from the route)
  numericId: number | null = null;
  // Button disable state and label
  updateDisabled: boolean = false;
  updateBtnLabel: string = 'Update Equipment';

  constructor(
    private route: ActivatedRoute, // Used to fetch parameters from the route
    private router: Router, // Used for navigating to different routes
    private backendCommunicationService: BackendCommunicationService // Service for backend API calls
  ) { }

  ngOnInit() {
    // Fetching the equipment ID from the route parameters
    const idParam = this.route.snapshot.paramMap.get('id');

    // Attempting to parse the ID as a number
    this.numericId = idParam ? parseInt(idParam, 10) : null;

    // If the ID is valid, fetch the equipment details
    if (this.numericId !== null && !isNaN(this.numericId)) {
      this.fetchEquipmentDetails();
    } else {
      // Alert the user if the ID is invalid
      alert('Invalid equipment ID. Please use a numeric ID.');
    }
  }

  // Fetch the equipment details from the backend
  fetchEquipmentDetails() {
    if (this.numericId !== null) {
      this.backendCommunicationService.getEditorEquipment(this.numericId).subscribe({
        next: (data) => {
          this.equipment = data; // Assign the retrieved data to equipment object
          this.checkFormCompletion(); // Check form completion state
        },
        error: (error) => {
          alert('Failed to load equipment details.'); // Alert in case of an error
        }
      });
    }
  }

  // Update the equipment data if the form is valid
  updateEquipment(event: MouseEvent) {
    if (
      event.type === 'click' && // Ensure the event is a click
      this.equipment.equipmentNumber && this.equipment.name && this.equipment.description &&
      this.equipment.type && Array.isArray(this.equipment.orders) && this.numericId !== null
    ) {
      this.backendCommunicationService.updateEditorEquipment(this.numericId, this.equipment)
        .pipe(
          catchError((error) => {
            alert('Failed to update equipment. Please try again.'); // Error handling
            return of(null); // Return null in case of error
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              alert('Equipment updated successfully!'); // Show success message
              this.router.navigateByUrl('/editor/equipment'); // Navigate back to equipment list
            }
          }
        });
    } else {
      alert('Please fill in all fields before updating.'); // Alert if form is incomplete
    }
  }

  // Check if the form is complete by validating the equipment details
  checkFormCompletion() {
    this.updateDisabled = !(
      this.equipment.equipmentNumber && this.equipment.name && this.equipment.description &&
      this.equipment.type && Array.isArray(this.equipment.orders)
    );
  }

  // Handle button click for navigating to the equipment editing page
  resolveButtonClick($event: any, action: string): void {
    if ($event.type === 'click' && action === 'edit') {
      if (!this.equipment || this.numericId === null) {
        alert('You must specify an equipment with a valid ID!'); // Alert if no equipment is selected
      } else {
        this.router.navigate(['/editor/equipment', this.numericId]); // Navigate to the edit equipment page
      }
    }
  }
}
