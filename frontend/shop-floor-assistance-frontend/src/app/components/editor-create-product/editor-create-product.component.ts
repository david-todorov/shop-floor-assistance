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
  imports: [FormsModule, ProductTableComponent, ButtonComponent, EditorProductComponent],
  templateUrl: './editor-create-product.component.html',
  styleUrls: ['./editor-create-product.component.css']
})

export class EditorCreateProductComponent {
  product: productTO = {
    number: '',
    name: '',
    description: '',
    language:'',
    country:'',
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

  // Method to handle Save Equipment button click
  //  saveEquipment(event: MouseEvent) {
  //   if (event.type==='click' && this.equipment.number && this.equipment.name && this.equipment.description) {
  //     this.backendCommunicationService.createEquipment(this.equipment).pipe(
  //       catchError(error => {
  //         console.error('Error creating equipment:', error);
  //         return of(null);
  //       })
  //     ).subscribe({
  //       next: (response) => {
  //         if (response) {
  //           console.log('Equipment created successfully:', response);
  //           // Display a success message
  //           alert('Equipment created successfully!');

  //           // Delay navigation to allow the user to see the message
  //           setTimeout(() => {
  //             this.router.navigateByUrl('/editor-equipment');
  //           }, 1000); // 1-second delay
  //         }
  //       }
  //     });
  //   } else {
  //     console.error('All fields are required');
  //   }
  // }

  // Method to handle Save Equipment button click with error handling
  saveProduct(event: MouseEvent) {
    if (event.type === 'click' && this.product.number && this.product.name && this.product.description) {
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
                this.router.navigateByUrl('/editor-product');
              }, 1000); // 1-second delay
            }
          },
          error: () => {
            // Handle any additional errors if needed
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
    this.createDisabled = !(this.product.number && this.product.name && this.product.description);
  }
}