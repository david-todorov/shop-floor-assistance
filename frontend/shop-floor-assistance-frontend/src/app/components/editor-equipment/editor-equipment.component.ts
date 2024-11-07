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
  editDisabled: boolean = true;
  createDisabled: boolean = false;
  editBtnLabel: string = 'Edit Equipment';
  createBtnLabel: string = 'Create Equipment';

  // newEquipmentData: any = {
  //   number:'',
  //   name: '',
  //   description: ''
  // };

  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  ngOnInit(): void {
    this.loadEquipment();
  }

  loadEquipment(): void {
    this.backendCommunicationService.getEditorEquipment().pipe(
      catchError((err) => {
        console.error('Error fetching equipment:', err);
        return of([]);
      })
    ).subscribe({
      next: (response) => {
        if (response) {
          this.loadEquipment = response; // Assign API response to loadedEquipment
          console.log('Equipment loaded:', this.loadEquipment);
        }
      },
      error: (err) => {
        console.log('An error occurred:', err);
      },
      complete: () => {
        console.log('Equipment retrieval completed');
      }
    });
  }

  equipmentSelected($event: any): void {
    this.equipment = $event;
  }

  resolveButtonClick($event: any, action: string): void {
   if ($event.type === 'click') {
      if (action === 'create') {
        // Directly navigate to the create equipment route without checking for equipment
        this.router.navigateByUrl('/editor-equipment/create');
      } else if (action === 'edit') {
        if (this.equipment == null || this.equipment === undefined) {
          alert('You must specify an equipment!');
        } else {
          console.log(this.equipment);
          this.router.navigateByUrl('/editor/equipment'),( this.equipment.number);
        }
      }

    }
    return;
  }

}