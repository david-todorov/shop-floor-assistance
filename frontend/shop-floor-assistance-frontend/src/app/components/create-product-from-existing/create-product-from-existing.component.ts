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
export class CreateProductFromExistingComponent {
  product: productTO = {
    id: 0,
    productNumber: '',
    name: '',
    description: '',
    language:'',
    country:'',
    type: '',
    packageSize: '',
    packageType: '',
  };
  selectedProduct: productTO | null = null;
  suggestions: productTO[] = []; // Store fetched suggestions
  createDisabled: boolean = true;
  createBtnLabel: string = 'Create Product';
  productNumberExists: boolean = false; // To track uniqueness of equipment number
  
  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService,
  ) { }

  ngOnInit() {
    this.loadSuggestions();
  }

  loadSuggestions() {
    // Fetch suggestions from the backend
    this.backendCommunicationService.getProductSuggestions().subscribe(
      (data) => {
        this.suggestions = data;
      },
      (error) => {
        console.error('Error fetching suggestions:', error);
      }
    );
  }

  selectSuggestion(suggestion: productTO) {
    console.log("Suggestion selected:", suggestion); // Debugging line
    this.selectedProduct = { ...suggestion, productNumber: '' };
    //this.createDisabled = true; // Disable the button until the new equipment number is unique
  }

    checkUniqueProductNumber() {
    if (this.selectedProduct) {
      // Check if the equipment number exists in suggestions
      this.productNumberExists = this.suggestions.some(
        (suggestion) => suggestion.productNumber === this.selectedProduct!.productNumber
      );

      // Enable the button only if all fields are filled and the equipment number is unique
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

  createProduct(event: MouseEvent) {
    if (event.type === 'click' && !this.productNumberExists) {
      console.log("Payload being sent:", this.selectedProduct);
      this.backendCommunicationService.createProduct(this.selectedProduct)
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
      console.error('Product number must be unique');
      alert('Please enter a unique product number.');
    }
  }

}
