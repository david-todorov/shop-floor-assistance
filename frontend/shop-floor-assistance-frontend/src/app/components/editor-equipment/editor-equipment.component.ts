import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { EquipmentTableComponent } from '../../shared/component-elements/equipment-table/equipment-table.component';
import { equipmentTO } from '../../shared/types/equipmentTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';

@Component({
  selector: 'app-editor-equipment', // Component selector for the editor equipment page
  standalone: true, // Indicates this is a standalone Angular component
  imports: [EquipmentTableComponent, ButtonComponent], // Declares dependencies used in the component
  templateUrl: './editor-equipment.component.html', // Template file for the component
  styleUrl: './editor-equipment.component.css' // Stylesheet for the component
})
export class EditorEquipmentComponent implements OnInit {

  // Variables for managing the selected equipment and equipment list
  equipment!: equipmentTO; // The currently selected equipment
  loadedEquipment!: equipmentTO[]; // List of all equipment fetched from the backend

  // Variables to manage the state of buttons
  editDisabled: boolean = true; // Disable "Edit" button by default
  createDisabled: boolean = false; // Enable "Create" button by default
  deleteDisabled: boolean = true; // Disable "Delete" button by default

  // Labels for the action buttons
  editBtnLabel: string = 'Edit Equipment';
  deleteBtnLabel: string = 'Delete Equipment';
  createBtnLabel: string = 'Create Equipment';

  constructor(
    private router: Router, // Router service for navigation
    private backendCommunicationService: BackendCommunicationService // Service for backend communication
  ) { }

  // Lifecycle hook that triggers on component initialization
  ngOnInit(): void {
    this.loadAllEquipment(); // Load all equipment from the backend
  }

  /**
   * Fetches all equipment from the backend and updates the loadedEquipment array.
   */
  loadAllEquipment(): void {
    this.backendCommunicationService.getAllEditorEquipment().pipe(
      // Handle errors during the API call
      catchError((err) => {
        console.error('Error fetching equipment:', err); // Log error if fetching fails
        return of([]); // Return an empty array as fallback
      })
    ).subscribe({
      next: (response: equipmentTO[]) => {
        this.loadedEquipment = response; // Assign the API response to loadedEquipment
      },
      error: (err) => {
        console.error('An error occurred while loading equipment:', err); // Log error if subscription fails
      },
      complete: () => {
        // Optional: Can be used to handle post-completion logic
      }
    });
  }

  /**
   * Handles the selection of equipment from the table.
   * @param $event - The selected equipment object
   */
  equipmentSelected($event: any) {
    this.equipment = $event; // Assign the selected equipment
    // Enable or disable buttons based on the selected equipment
    this.deleteDisabled = !this.equipment || !this.equipment.id;
    this.editDisabled = !this.equipment || !this.equipment.id;
  }

  /**
   * Resolves button click actions for "Create", "Edit", and "Delete".
   * @param $event - Click event
   * @param action - The action type (create, edit, delete)
   */
  resolveButtonClick($event: any, action: string): void {
    if ($event.type === 'click') {
      if (action === 'create') {
        // Navigate to the equipment creation page
        this.router.navigateByUrl('/editor-equipment/creation-option');
      } else if (action === 'edit') {
        if (!this.equipment || this.equipment.id === undefined) {
          alert('You must specify an equipment with a valid ID!');
        } else {
          this.router.navigate(['/editor/equipment', this.equipment.id]); // Navigate to the equipment edit page
        }
      } else if (action === 'delete') {
        this.deleteEquipment(); // Trigger the delete action
      }
    }
  }

  /**
   * Deletes the selected equipment after confirmation.
   */
  deleteEquipment(): void {
    if (!this.equipment || !this.equipment.id) {
      alert('You must select equipment with a valid ID to delete!');
      return;
    }

    // Confirm deletion with the user
    const confirmDelete = confirm(
      `Are you sure you want to delete the equipment: ${this.equipment.name} (ID: ${this.equipment.id})?`
    );

    if (confirmDelete) {
      // Call the backend to delete the selected equipment
      this.backendCommunicationService.deleteEditorEquipment(this.equipment.id).subscribe({
        next: () => {
          alert(`Equipment with ID ${this.equipment.id} deleted successfully.`);
          // Remove the deleted equipment from the loadedEquipment array
          this.loadedEquipment = this.loadedEquipment.filter(
            (e) => e.id !== this.equipment.id
          );
          // Clear the selected equipment and disable the delete button
          this.equipment = undefined!;
          this.deleteDisabled = true;
        },
        error: (error) => {
          console.error('Error deleting equipment:', error); // Log error if delete fails
          alert('Failed to delete the equipment. Please try again.');
        },
      });
    }
  }

}
