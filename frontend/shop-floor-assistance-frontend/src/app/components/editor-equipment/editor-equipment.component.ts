import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { EquipmentTableComponent } from '../../shared/component-elements/equipment-table/equipment-table.component';
import { equipmentTO } from '../../types/equipmentTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';

@Component({
  selector: 'app-editor-equipment',
  standalone: true,
  imports: [EquipmentTableComponent, ButtonComponent],
  templateUrl: './editor-equipment.component.html',
  styleUrl: './editor-equipment.component.css'
})

export class EditorEquipmentComponent implements OnInit {

  equipment!: equipmentTO;
  loadedEquipment!: equipmentTO[];
  editDisabled: boolean = false;
  createDisabled: boolean = false;
  editBtnLabel: string = 'Edit / Delete';
  createBtnLabel: string = 'Create Equipment';


  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  ngOnInit(): void {
    this.loadAllEquipment();
  }

  loadAllEquipment(): void {
    this.backendCommunicationService.getAllEditorEquipment().pipe(
      catchError((err) => {
        console.error('Error fetching equipment:', err);
        return of([]);
      })
    ).subscribe({
      next: (response: equipmentTO[]) => {
        this.loadedEquipment = response; // Directly assign the API response to loadedEquipment
        console.log('Equipment loaded:', this.loadedEquipment);
      },
      error: (err) => {
        console.error('An error occurred while loading equipment:', err);
      },
      complete: () => {
        console.log('Equipment loading complete');
      }
    });
  }

  equipmentSelected($event: any) {
    this.equipment = $event
  }

  resolveButtonClick($event: any, action: string): void {
    if ($event.type === 'click') {
      if (action === 'create') {
        // Directly navigate to the create equipment route without checking for equipment
        this.router.navigateByUrl('/editor-equipment/creation-option');
      } else if (action === 'edit') {
        if (!this.equipment || this.equipment.id === undefined) {
          alert('You must specify an equipment with a valid ID!');
        } else {
          console.log('Selected equipment:', this.equipment);
          this.router.navigate(['/editor/equipment', this.equipment.id]); // Use the numeric ID for navigation
        }
      }

    }
    return;
  }

}