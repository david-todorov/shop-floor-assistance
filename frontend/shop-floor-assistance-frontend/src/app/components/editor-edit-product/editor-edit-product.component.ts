import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { productTO } from '../../types/productTO';
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

  numericId: number | null = null;
  updateDisabled: boolean = false;
  updateBtnLabel: string = 'Update Product';
  deleteBtnLabel: string = "Delete Product";

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  ngOnInit() {
    // Get the ID from the route
    const idParam = this.route.snapshot.paramMap.get('id');

    // Attempt to parse it as a numeric ID
    this.numericId = idParam ? parseInt(idParam, 10) : null;

    // Check if the ID is a valid number
    if (this.numericId !== null && !isNaN(this.numericId)) {
      this.fetchProductDetails();
    } else {
      console.error('Invalid numeric ID:', idParam);
      alert('Invalid product ID. Please use a numeric ID.');
      // Optionally redirect to a different page or handle this error as needed
    }
  }
  fetchProductDetails() {
     if (this.numericId !== null) {
      this.backendCommunicationService.getEditorProduct(this.numericId).subscribe({
        next: (data) => {
          this.product = data;
          console.log('Fetched product details:', this.product); // Log the product details for debugging
          this.checkFormCompletion(); 
        },
        error: (error) => {
          console.error('Error loading product:', error);
          alert('Failed to load product details.');
        }
      });
    }
  }

  updateProduct(event: MouseEvent) {
    if (
      event.type === 'click' &&
      this.product.productNumber &&
      this.product.name &&
      this.product.description &&
      this.product.type &&
      this.product.language &&
      this.product.packageType &&
      this.product.country &&
      this.product.packageSize &&
      this.numericId !== null
    ) {
      this.backendCommunicationService.updateEditorProduct(this.numericId, this.product)
        .pipe(
          catchError((error) => {
            console.error('Error updating product:', error);
            alert('Failed to update product. Please try again.');
            return of(null);
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              alert('Product updated successfully!');
              this.router.navigateByUrl('/editor/products');
            }
          }
        });
    } else {
      alert('Please fill in all fields before updating.');
    }
  }

  checkFormCompletion() {
    this.updateDisabled = !(
      this.product.productNumber &&
      this.product.name &&
      this.product.description &&
      this.product.type &&
      this.product.language &&
      this.product.packageType &&
      this.product.country &&
      this.product.packageSize
    );
  }

  deleteProduct() {
    if (confirm('Are you sure you want to delete this Product? This action cannot be undone.')) {
      if (this.numericId !== null) {
        this.backendCommunicationService.deleteEditorProduct(this.numericId).subscribe({
          next: () => {
            alert('Product deleted successfully!');
            this.router.navigateByUrl('/editor/products');
          },
          error: (error) => {
            console.error('Error deleting product:', error);
            alert('Failed to delete product. Please try again.');
          }
        });
      } else {
        alert('Invalid product ID. Cannot delete.');
      }
    }
  }

  resolveButtonClick($event: any, action: string): void {
    if ($event.type === 'click') {
      if (action === 'create') {
        this.router.navigateByUrl('/editor-product/create');
      } else if (action === 'edit') {
        if (!this.product || this.numericId === null) {
          alert('You must specify a product with a valid ID!');
        } else {
          this.router.navigate(['/editor/products', this.numericId]);
        }
      } else if (action === 'delete') {
        this.deleteProduct();
      }
    }
  }
}
