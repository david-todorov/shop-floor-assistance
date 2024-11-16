import { FormsModule } from '@angular/forms'; // Import FormsModule
import { ActivatedRoute, Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { ProductTableComponent } from '../../shared/component-elements/product-table/product-table.component';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { EditorProductComponent } from '../editor-product/editor-product.component';
import { productTO } from '../../types/productTO';
import { Component } from '@angular/core';

@Component({
  selector: 'app-editor-create-product',
  standalone: true,
  imports: [FormsModule, ButtonComponent],
  templateUrl: './editor-create-product.component.html',
  styleUrls: ['./editor-create-product.component.css']
})

export class EditorCreateProductComponent {
  product: productTO = {
    productNumber: '',
    name: '',
    description: '',
    language:'',
    country:'',
    type: '',
    packageSize: '',
    packageType: '',
  };
  createDisabled: boolean = true;
  createBtnLabel: string = 'Save Product';

  // Initialize equipmentState without an interface
  productState = {
    buttonIcon: 'save',
    buttonLabel: 'Save Product',
    isSaved: false
  };


  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  // Method to handle Save Product button click with error handling
  saveProduct(event: MouseEvent) {
    if (
      event.type === 'click' &&
      this.product.productNumber &&
      this.product.name &&
      this.product.description &&
      this.product.language &&
      this.product.country &&
      this.product.type &&
      this.product.packageSize &&
      this.product.packageType
    ) {
      console.log("Payload:", this.product); // Debugging line to check payload
      this.backendCommunicationService.createProduct(this.product)
        .pipe(
          catchError((error) => {
            console.error('Error creating product:', error);
            alert('Failed to create product. Please try again.');
            return of(null); // Return null to continue with the observable flow
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              console.log('Product created successfully:', response);

              // Update UI states
              this.productState.buttonIcon = 'check_circle';
              this.productState.buttonLabel = 'Saved';
              this.productState.isSaved = true;

              alert('Product created successfully!');

              // Delay navigation to allow the user to see the message
              setTimeout(() => {
                this.router.navigateByUrl('/editor/products');
              }, 1000); // 1-second delay
            }
          },
          error: () => {
            console.error('Unexpected error occurred during product creation.');
          }
        });
    } else {
      console.error('All fields are required');
      alert('Please fill in all fields before saving.');
    }
  }

  // Enable the button when all fields have values
  checkFormCompletion() {
    console.log("Product Number:", this.product.productNumber);
    console.log("Name:", this.product.name);
    console.log("Description:", this.product.description);
    console.log("Language:", this.product.language);
    console.log("Country:", this.product.country);
    console.log("Type:", this.product.type);
    console.log("Package Size:", this.product.packageSize);
    console.log("Package Type:", this.product.packageType);

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
    console.log("Create Disabled:", this.createDisabled);
  }
}