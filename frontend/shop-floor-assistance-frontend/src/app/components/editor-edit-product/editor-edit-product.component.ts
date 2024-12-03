import { Component } from '@angular/core'; 
import { ActivatedRoute, Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { productTO } from '../../shared/types/productTO';
import { catchError, of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';

@Component({
  selector: 'app-editor-edit-product',
  standalone: true,
  imports: [FormsModule, ButtonComponent],
  templateUrl: './editor-edit-product.component.html',
  styleUrl: './editor-edit-product.component.css'
})
export class EditorEditProductComponent {

  // Initial product object with default values
  product: productTO = {
    id: 0,
    productNumber: "",
    name: "",
    description: "",
    language: "",
    country: "",
    packageSize: "",
    packageType: "",
    type: "",
  };

  // Variable for storing the numeric ID from the route
  numericId: number | null = null;
  // Button disabled state and label
  updateDisabled: boolean = false;
  updateBtnLabel: string = 'Update Product';

  constructor(
    private route: ActivatedRoute, // Activated route for fetching parameters from the URL
    private router: Router, // Router for navigation
    private backendCommunicationService: BackendCommunicationService // Backend service for product API calls
  ) { }

  ngOnInit() {
    // Get the product ID from the URL route
    const idParam = this.route.snapshot.paramMap.get('id');

    // Attempt to parse the ID as a number
    this.numericId = idParam ? parseInt(idParam, 10) : null;

    // Check if the parsed ID is a valid number
    if (this.numericId !== null && !isNaN(this.numericId)) {
      this.fetchProductDetails(); // Fetch product details if the ID is valid
    } else {
      alert('Invalid product ID. Please use a numeric ID.'); // Alert if the ID is invalid
    }
  }

  // Fetch product details from the backend
  fetchProductDetails() {
    if (this.numericId !== null) {
      this.backendCommunicationService.getEditorProduct(this.numericId).subscribe({
        next: (data) => {
          this.product = data; // Assign the fetched product data
          this.checkFormCompletion(); // Check form completion status
        },
        error: (error) => {
          alert('Failed to load product details.'); // Alert in case of an error
        }
      });
    }
  }

  // Update the product using the backend service
  updateProduct(event: MouseEvent) {
    if (
      event.type === 'click' && // Ensure the event is a click
      this.product.productNumber && this.product.name && this.product.description &&
      this.product.type && this.product.language && this.product.packageType &&
      this.product.country && this.product.packageSize && this.numericId !== null
    ) {
      this.backendCommunicationService.updateEditorProduct(this.numericId, this.product)
        .pipe(
          catchError((error) => {
            alert('Failed to update product. Please try again.'); // Handle update error
            return of(null); // Return null in case of error
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              alert('Product updated successfully!'); // Alert on successful update
              this.router.navigateByUrl('/editor/products'); // Navigate to product list
            }
          }
        });
    } else {
      alert('Please fill in all fields before updating.'); // Alert if any field is missing
    }
  }

  // Check if the form is completed (all fields filled)
  checkFormCompletion() {
    this.updateDisabled = !(
      this.product.productNumber && this.product.name && this.product.description &&
      this.product.type && this.product.language && this.product.packageType &&
      this.product.country && this.product.packageSize
    );
  }

  // Handle button click for editing product
  resolveButtonClick($event: any, action: string): void {
    if ($event.type === 'click' && action === 'edit') {
      if (!this.product || this.numericId === null) {
        alert('You must specify a product with a valid ID!'); // Alert if no valid product is selected
      } else {
        this.router.navigate(['/editor/products', this.numericId]); // Navigate to edit product page
      }
    }
  }
}
