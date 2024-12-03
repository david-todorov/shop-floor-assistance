import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { ProductTableComponent } from '../../shared/component-elements/product-table/product-table.component';
import { productTO } from '../../shared/types/productTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';

@Component({
  selector: 'app-editor-product',
  standalone: true,
  imports: [ProductTableComponent, ButtonComponent],
  templateUrl: './editor-product.component.html',
  styleUrl: './editor-product.component.css'
})
export class EditorProductComponent implements OnInit {

  // Variables to track the selected product and loaded product list
  product!: productTO; // Selected product
  loadedProduct!: productTO[]; // List of products from the API

  // State variables for button enabling/disabling
  editDisabled: boolean = true;
  deleteDisabled: boolean = true;
  createDisabled: boolean = false;

  // Labels for the buttons
  editBtnLabel: string = 'Edit Product';
  deleteBtnLabel: string = 'Delete Product';
  createBtnLabel: string = 'Create Product';

  constructor(
    private router: Router, // Router for navigation
    private backendCommunicationService: BackendCommunicationService // Service for backend communication
  ) { }

  // Lifecycle hook to initialize component and load products
  ngOnInit(): void {
    this.loadProducts();
  }

  // Function to fetch all products from the backend
  loadProducts(): void {
    this.backendCommunicationService.getAllEditorProduct().pipe(
      catchError((err) => {
        console.error('Error fetching product:', err); // Log error if API call fails
        return of([]); // Return an empty array as fallback
      })
    ).subscribe({
      next: (response: productTO[]) => {
        this.loadedProduct = response; // Assign API response to `loadedProduct`
      },
      error: (err) => {
        console.error('An error occurred while loading product', err); // Log error during subscription
      },
      complete: () => {
        // Action after products are successfully loaded (if any)
      }
    });
  }

  // Function to handle product selection from the table
  productSelected($event: any) {
    this.product = $event; // Assign selected product
    // Enable/disable buttons based on product selection
    this.deleteDisabled = !this.product || !this.product.id;
    this.editDisabled = !this.product || !this.product.id;
  }

  // Function to handle button actions for create, edit, and delete
  resolveButtonClick($event: any, action: string): void {
    if ($event.type === 'click') {
      if (action === 'create') {
        // Navigate to the product creation page
        this.router.navigateByUrl('/editor-product/creation-option');
      } else if (action === 'edit') {
        // Check if a product is selected before navigating to edit page
        if (this.product == null || this.product === undefined) {
          alert('You must specify a product!');
        } else {
          this.router.navigate(['/editor/product/', this.product.id]); // Navigate to edit page
        }
      } else if (action === 'delete') {
        this.deleteProduct(); // Call delete function
      }
    }
  }

  // Function to delete a selected product
  deleteProduct(): void {
    if (!this.product || !this.product.id) {
      alert('You must select a product with a valid ID to delete!'); // Alert if no product is selected
      return;
    }

    const confirmDelete = confirm(
      `Are you sure you want to delete the product: ${this.product.name} (ID: ${this.product.id})?`
    );

    if (confirmDelete) {
      // Call the backend service to delete the product
      this.backendCommunicationService.deleteEditorProduct(this.product.id).subscribe({
        next: () => {
          alert(`Product with ID ${this.product.id} deleted successfully.`); // Alert success
          // Remove the deleted product from the loaded product array
          this.loadedProduct = this.loadedProduct.filter(
            (e) => e.id !== this.product.id
          );
          // Reset product selection and disable delete button
          this.product = undefined!;
          this.deleteDisabled = true;
        },
        error: (error) => {
          console.error('Error deleting product:', error); // Log error if delete fails
          alert('Failed to delete the product. Please try again.'); // Alert failure
        },
      });
    }
  }
}
