import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { catchError, map, of } from 'rxjs';
import { ButtonComponent } from "../../shared/component-elements/button/button.component";
import { equipmentTO } from '../../shared/types/equipmentTO';
import { productTO } from '../../shared/types/productTO';
import { orderTO } from '../../shared/types/orderTO';

// Default product used as a placeholder in the order
const defaultProduct: productTO = {
  id: 0,
  productNumber: "DEFAULT",
  name: "Default Product",
  language: "N/A",
  type: "N/A",
  country: "N/A",
  packageSize: "N/A",
  packageType: "N/A",
  description: "Default product description"
};

@Component({
  selector: 'app-editor-create-order',
  standalone: true,
  imports: [FormsModule, CommonModule, ButtonComponent],
  templateUrl: './editor-create-order.component.html',
  styleUrl: './editor-create-order.component.css'
})
export class EditorCreateOrderComponent implements OnInit {
  // Order object to store the details of the order
  order: orderTO = {
    orderNumber: "",
    name: "",
    description: "",
    productAfter: defaultProduct,
    productBefore: defaultProduct,
    equipment: [],
    workflows: []
  };

  // List of available equipment
  equipmentList: equipmentTO[] = [];
  // Array for storing selected equipment
  selectedEquipment: equipmentTO[] = [];
  // List of available products
  productList: productTO[] = [];
  // Currently selected product before and after
  selectedProductBefore: productTO | null = null;
  selectedProductAfter: productTO | null = null;

  // Controls whether the Create button is enabled or not
  createDisabled: boolean = false;
  // Label for the Create button
  createBtnLabel: string = 'Create';

  // UI state for the order creation button
  orderState = {
    buttonIcon: 'save',
    buttonLabel: 'Save Order',
    isSaved: false
  };

  constructor(
    private router: Router, // Router for navigation
    private backendCommunicationService: BackendCommunicationService // Backend service for API calls
  ) { }

  // Lifecycle hook to initialize data on component load
  ngOnInit() {
    this.fetchEquipment(); // Fetch available equipment
    this.fetchProducts(); // Fetch available products
  }

  // Fetch all equipment from the backend
  fetchEquipment() {
    this.backendCommunicationService.getAllEditorEquipment()
      .pipe(
        map((response: any[]) => response.map(item => ({
          id: item.id,
          equipmentNumber: item.equipmentNumber,
          name: item.name,
          type: item.type,
          description: item.description,
          createdBy: item.createdBy,
          updatedBy: item.updatedBy,
          createdAt: item.createdAt,
          updatedAt: item.updatedAt,
          orders: item.orders
        }) as equipmentTO)),
        catchError(error => {
          alert('Error fetching equipment');
          return of([]); // Return empty array if error occurs
        })
      )
      .subscribe((equipment: equipmentTO[]) => this.equipmentList = equipment); // Populate the equipment list
  }

  // Handle change in selected equipment and update the order equipment
  onEquipmentChange(selectedEquipments: equipmentTO[]) {
    this.order.equipment = [...selectedEquipments]; // Update order with selected equipment
  }

  // Fetch all products from the backend
  fetchProducts() {
    this.backendCommunicationService.getAllEditorProduct()
      .pipe(
        map((response: any[]) => response.map(item => ({
          id: item.id,
          productNumber: item.productNumber,
          name: item.name,
          language: item.language,
          country: item.country,
          packageSize: item.packageSize,
          packageType: item.packageType
        }) as productTO)),
        catchError(error => {
          alert('Error fetching products');
          return of([]); // Return empty array if error occurs
        })
      )
      .subscribe((products: productTO[]) => this.productList = products); // Populate the product list
  }

  // Handle change in selected product before and update the order
  onProductBeforeChange(selectedProductBefore: productTO) {
    this.order.productBefore = selectedProductBefore; // Update order's productBefore
  }

  // Handle change in selected product after and update the order
  onProductAfterChange(selectedProductAfter: productTO) {
    this.order.productAfter = selectedProductAfter; // Update order's productAfter
  }

  // Save the order if the form is complete
  saveOrder(event: MouseEvent) {
    if (event.type === 'click' && this.isFormComplete()) {
      // Call the backend service to create the order
      this.backendCommunicationService.createOrder(this.order)
        .pipe(
          catchError((error) => {
            alert('Failed to create order. Please try again.'); // Handle error
            return of(null); // Continue observable flow
          })
        )
        .subscribe({
          next: (response: any) => {
            if (response && response.id) {
              // Update UI state after successful creation
              this.orderState.buttonIcon = 'check_circle';
              this.orderState.buttonLabel = 'Saved';
              this.orderState.isSaved = true;

              alert('Order created successfully!');

              // Delay navigation to allow user to see the success message
              setTimeout(() => {
                this.router.navigate(['/editor/orders', response.id]); // Redirect to created order's page
              }, 1000); // 1-second delay
            }
          },
          error: () => {
            alert('Unexpected error occurred during order creation.'); // Alert if there are unexpected errors
          }
        });
    } else {
      alert('Please fill in all fields before saving.'); // Alert if form is incomplete
    }
  }

  // Check if the form is complete
  isFormComplete(): boolean {
    return !!(
      this.order.orderNumber &&
      this.order.name &&
      this.order.description &&
      this.order.equipment.length > 0 &&
      this.order.productBefore &&
      this.order.productAfter
    );
  }

  // Check form completion status and enable/disable create button
  checkFormCompletion() {
    this.createDisabled = !this.isFormComplete();
  }
}
