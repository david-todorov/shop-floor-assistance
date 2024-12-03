// Importing necessary Angular modules and components
import { Component, Input } from '@angular/core';
import { MatListModule } from '@angular/material/list'; // Angular Material module for list UI
import { ButtonComponent } from '../button/button.component'; // Custom ButtonComponent for interaction
import { CommonModule } from '@angular/common'; // Common Angular module for core directives

@Component({
  selector: 'app-list', // Selector for the list component
  standalone: true, // Indicates the component is standalone
  imports: [
    MatListModule, // Material List module for displaying lists
    ButtonComponent, // Custom button component for interactions within the list
    CommonModule // Common module for Angular directives like ngFor, ngIf, etc.
  ],
  templateUrl: './list.component.html', // Path to the component's HTML template
  styleUrl: './list.component.css' // Path to the component's CSS styles
})
export class ListComponent {
  @Input() items: string[] = []; // Input property for receiving a list of items as a string array
}
