import { Component, OnInit } from '@angular/core';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { catchError, of } from 'rxjs';
import { Router } from '@angular/router';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { productTO } from '../../shared/types/productTO';

@Component({
  selector: 'app-create-product-from-existing',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonComponent],
  templateUrl: './create-product-from-existing.component.html',
  styleUrl: './create-product-from-existing.component.css'
})

export class CreateProductFromExistingComponent implements OnInit {
  // Initializing a product object with default values
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
  
  selectedProduct: productTO | null = null; // Holds the selected product after suggestion selection
  suggestions: productTO[] = []; // Array to store product suggestions fetched from the backend
  createDisabled: boolean = true; // Controls whether the Create button is disabled
  createBtnLabel: string = 'Create Product'; // Label for the Create button
  productNumberExists: boolean = false; // Tracks whether the product number already exists

  constructor(
    private router: Router, // Router to handle navigation
    private backendCommunicationService: BackendCommunicationService, // Backend service to interact with the API
  ) { }

  // Lifecycle hook for component initialization
  ngOnInit() {
    this.loadSuggestions(); // Fetch product suggestions when the component initializes
  }

  // Fetch product suggestions from the backend
  loadSuggestions() {
    this.backendCommunicationService.getProductSuggestions().subscribe(
      (data) => {
        this.suggestions = data; // Store the fetched suggestions
      },
      (error) => {
        alert('Error fetching suggestions');
      }
    );
  }

  // Method for selecting a product suggestion and resetting the product number
  selectSuggestion(suggestion: productTO) {
    this.selectedProduct = { ...suggestion, productNumber: '' }; // Copy the suggestion and reset the product number
  }

  // Check if the selected product number is unique and validate the form
  checkUniqueProductNumber() {
    if (this.selectedProduct) {
      // Check if the selected product number already exists in the suggestions
      this.productNumberExists = this.suggestions.some(
        (suggestion) => suggestion.productNumber === this.selectedProduct!.productNumber
      );

      // Enable the create button only if the product number is unique and all fields are filled
      this.createDisabled = this.productNumberExists || !(
        this.selectedProduct.productNumber &&
        this.selectedProduct.name &&
        this.selectedProduct.description &&
        this.selectedProduct.type &&
        this.selectedProduct.country &&
        this.selectedProduct.language &&
        this.selectedProduct.packageSize &&
        this.selectedProduct.packageType
      );
    }
  }

  // Create the product if the form is valid and product number is unique
  createProduct(event: MouseEvent) {
    if (event.type === 'click' && !this.productNumberExists) {
      // Call the backend service to create the product
      this.backendCommunicationService.createProduct(this.selectedProduct)
        .pipe(
          catchError((error) => {
            alert('Failed to create product. Please try again.');
            return of(null); // Return null to continue the observable flow
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              alert('Product created successfully!');
              setTimeout(() => {
                this.router.navigateByUrl('/editor/products'); // Redirect to the products page after creation
              }, 1000); // 1-second delay
            }
          },
          error: () => {
            alert('Unexpected error occurred during product creation.');
          }
        });
    } else {
      alert('Please enter a unique product number.'); // Alert if the product number is not unique
    }
  }
}
