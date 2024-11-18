import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { equipmentTO } from '../../types/equipmentTO';
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
  equipment: equipmentTO = {
    equipmentNumber: "",
    name: "",
    description: "",
    type: "",
    orders: [],
  };

  numericId: number | null = null;
  updateDisabled: boolean = false;
  updateBtnLabel: string = 'Update Equipment';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  ngOnInit() {
    // Get the ID from the route
    const idParam = this.route.snapshot.paramMap.get('id');

    // Attempt to parse it as a numeric ID
    this.numericId = idParam ? parseInt(idParam, 10) : null;

    // Check if the ID is a valid number
    if (this.numericId !== null && !isNaN(this.numericId)) {
      this.fetchEquipmentDetails();
    } else {
      console.error('Invalid numeric ID:', idParam);
      alert('Invalid equipment ID. Please use a numeric ID.');
    }
  }

  fetchEquipmentDetails() {
    if (this.numericId !== null) {
      this.backendCommunicationService.getEditorEquipment(this.numericId).subscribe({
        next: (data) => {
          this.equipment = data;
          this.checkFormCompletion();
        },
        error: (error) => {
          console.error('Error loading equipment:', error);
          alert('Failed to load equipment details.');
        }
      });
    }
  }

  updateEquipment(event: MouseEvent) {
    if (
      event.type === 'click' &&
      this.equipment.equipmentNumber &&
      this.equipment.name &&
      this.equipment.description &&
      this.equipment.type &&
      Array.isArray(this.equipment.orders) &&
      this.numericId !== null
    ) {
      this.backendCommunicationService.updateEditorEquipment(this.numericId, this.equipment)
        .pipe(
          catchError((error) => {
            console.error('Error updating equipment:', error);
            alert('Failed to update equipment. Please try again.');
            return of(null);
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              alert('Equipment updated successfully!');
              this.router.navigateByUrl('/editor/equipment');
            }
          }
        });
    } else {
      alert('Please fill in all fields before updating.');
    }
  }

  checkFormCompletion() {
    this.updateDisabled = !(
      this.equipment.equipmentNumber &&
      this.equipment.name &&
      this.equipment.description &&
      this.equipment.type &&
      Array.isArray(this.equipment.orders)
    );
  }

  resolveButtonClick($event: any, action: string): void {
  if ($event.type === 'click' && action === 'edit') {
    if (!this.equipment || this.numericId === null) {
      alert('You must specify an equipment with a valid ID!');
    } else {
      this.router.navigate(['/editor/equipment', this.numericId]);
    }
  }
}


}