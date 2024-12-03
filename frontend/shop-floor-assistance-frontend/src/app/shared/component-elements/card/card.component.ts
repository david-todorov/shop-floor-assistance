// Importing necessary Angular modules
import { Component } from '@angular/core';
// Importing Angular Material Card module for card layout and styling
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-card', // Selector for using this component in templates
  standalone: true, // Indicates this component is standalone and does not rely on a module
  imports: [MatCardModule], // Declares the dependencies used in this component
  templateUrl: './card.component.html', // Path to the HTML template for this component
  styleUrl: './card.component.css' // Path to the CSS styles for this component
})
export class CardComponent {
  // This is a simple Angular component that leverages Angular Material's MatCardModule
}
