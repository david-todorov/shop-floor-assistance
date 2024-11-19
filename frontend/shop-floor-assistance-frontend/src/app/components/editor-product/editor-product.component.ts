import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { ProductTableComponent } from '../../shared/component-elements/product-table/product-table.component';
import { productTO } from '../../types/productTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';

@Component({
  selector: 'app-editor-product',
  standalone: true,
  imports: [ProductTableComponent, ButtonComponent],
  templateUrl: './editor-product.component.html',
  styleUrl: './editor-product.component.css'
})

export class EditorProductComponent implements OnInit {
  
  product!: productTO;
  loadedProduct!: productTO[];
  editDisabled: boolean = true;
  deleteDisabled: boolean = true;
  createDisabled: boolean = false;
  editBtnLabel: string = 'Edit Product';
  deleteBtnLabel: string = 'Delete Product';
  createBtnLabel: string = 'Create Product';

  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  ngOnInit(): void {
    this.loadProducts();
    console.log("Loading products in console")
  }

  loadProducts(): void {
    this.backendCommunicationService.getAllEditorProduct().pipe(
      catchError((err) => {
        console.error('Error fetching product:', err);
        return of([]);
      })
    ).subscribe({
      next: (response: productTO[]) => {
          this.loadedProduct = response; // Assign API response to loadedProduct
          console.log('Product loaded:', this.loadedProduct);
        },
      error: (err) => {
        console.log('An error occurred while loading product', err);
      },
      complete: () => {
        console.log('Products loaded complete');
      }
    });
  }

  productSelected($event: any) {
    this.product = $event;
    this.deleteDisabled = !this.product || !this.product.id;
    this.editDisabled = !this.product || !this.product.id;
  }

  resolveButtonClick($event: any, action: string): void {
   if ($event.type === 'click') {
      if (action === 'create') {
        // Directly navigate to the create equipment route without checking for equipment
        this.router.navigateByUrl('/editor-product/creation-option');
      } else if (action === 'edit') {
        if (this.product == null || this.product === undefined) {
          alert('You must specify a product!');
        } else {
          console.log('Selected product:', this.product);
          this.router.navigate(['/editor/product/', this.product.id]);
        }
      } else if (action === 'delete') {
        this.deleteProduct();
      }
    }
  }

   deleteProduct(): void {
    if (!this.product || !this.product.id) {
      alert('You must select a product with a valid ID to delete!');
      return;
    }

    const confirmDelete = confirm(
      `Are you sure you want to delete the product: ${this.product.name} (ID: ${this.product.id})?`
    );

    if (confirmDelete) {
      this.backendCommunicationService.deleteEditorProduct(this.product.id).subscribe({
        next: () => {
          alert(`Product with ID ${this.product.id} deleted successfully.`);
          // Remove the deleted product from the loadedProduct array
          this.loadedProduct = this.loadedProduct.filter(
            (e) => e.id !== this.product.id
          );
          // Clear the selected product and disable the delete button
          this.product = undefined!;
          this.deleteDisabled = true;
        },
        error: (error) => {
          console.error('Error deleting product:', error);
          alert('Failed to delete the product. Please try again.');
        },
      });
    }
  }

    confirmHome() {
this.router.navigateByUrl('/editor-homepage');
}
}