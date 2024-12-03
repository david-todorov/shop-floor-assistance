// Import statements for Angular components and other utilities
import { FormsModule } from '@angular/forms'; 
import { ActivatedRoute, Router } from '@angular/router'; 
import { BackendCommunicationService } from '../../services/backend-communication.service'; 
import { catchError, of } from 'rxjs'; 
import { ButtonComponent } from '../../shared/component-elements/button/button.component'; 
import { productTO } from '../../shared/types/productTO'; 
import { Component } from '@angular/core';

@Component({
  selector: 'app-editor-create-product', 
  standalone: true, 
  imports: [FormsModule, ButtonComponent], 
  templateUrl: './editor-create-product.component.html', 
  styleUrls: ['./editor-create-product.component.css'] 
})

export class EditorCreateProductComponent {
  // Initial product object with empty values
  product: productTO = {
    id: 0,
    productNumber: '',
    name: '',
    description: '',
    language: '',
    country: '',
    type: '',
    packageSize: '',
    packageType: '',
  };

  // Button state to control if the save button is disabled
  createDisabled: boolean = true;
  // Save button label
  createBtnLabel: string = 'Save Product';

  // Product state for button icon and label
  productState = {
    buttonIcon: 'save',
    buttonLabel: 'Save Product',
    isSaved: false
  };

  constructor(
    private router: Router, // Inject Router for navigation
    private backendCommunicationService: BackendCommunicationService // Inject backend service to interact with the API
  ) { }

  // Method to handle Save Product button click with error handling
  saveProduct(event: MouseEvent) {
    if (
      event.type === 'click' && // Ensure the event is a click
      this.product.productNumber &&
      this.product.name &&
      this.product.description &&
      this.product.language &&
      this.product.country &&
      this.product.type &&
      this.product.packageSize &&
      this.product.packageType
    ) {
      // Call backend service to create a new product
      this.backendCommunicationService.createProduct(this.product)
        .pipe(
          catchError((error) => {
            // Handle errors during product creation
            alert('Failed to create product. Please try again.');
            return of(null); // Return null to continue with the observable flow
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              // Update UI states upon successful creation
              this.productState.buttonIcon = 'check_circle';
              this.productState.buttonLabel = 'Saved';
              this.productState.isSaved = true;

              alert('Product created successfully!');

              // Delay navigation to allow the user to see the message
              setTimeout(() => {
                this.router.navigateByUrl('/editor/products'); // Navigate to product list after 1 second
              }, 1000);
            }
          },
          error: () => {
            // Handle unexpected errors during the product creation process
            alert('Unexpected error occurred during product creation.');
          }
        });
    } else {
      // Alert if any field is missing
      alert('Please fill in all fields before saving.');
    }
  }

  // Enable the save button when all fields are filled
  checkFormCompletion() {
    this.createDisabled = !(
      this.product.productNumber &&
      this.product.name &&
      this.product.description &&
      this.product.language &&
      this.product.country &&
      this.product.type &&
      this.product.packageSize &&
      this.product.packageType
    );
  }
}
