// Importing core Angular functionality
import { Component } from '@angular/core';
// Importing the custom ButtonComponent to be used in this component
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
// Importing Angular's Router service for navigation between routes
import { Router } from '@angular/router';

// Defining the component metadata
@Component({
  selector: 'app-editor-homepage', // The component's selector used in HTML
  standalone: true, // Indicates this component is standalone and doesn't require a module
  imports: [ButtonComponent], // Declares ButtonComponent as a dependency
  templateUrl: './editor-homepage.component.html', // Path to the HTML template
  styleUrl: './editor-homepage.component.css' // Path to the CSS stylesheet
})
export class EditorHomepageComponent {
  
  // Constructor for dependency injection of the Router service
  constructor(private router: Router) { }

  /**
   * Handles navigation based on the selected option.
   * @param index - The index of the selected option.
   */
  selectoption(index: number) {
    switch (index) {
      case 0:
        // Navigate to the orders editor page
        this.router.navigateByUrl('/editor/orders');
        break;
      case 1:
        // Navigate to the equipment editor page
        this.router.navigateByUrl('/editor/equipment');
        break;
      case 2:
        // Navigate to the products editor page
        this.router.navigateByUrl('/editor/products');
        break;
      default:
        // Handle invalid selections
        console.error('Invalid option selected');
    }
  }
}
