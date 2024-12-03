import { Component, OnInit } from '@angular/core';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { catchError, map, of } from 'rxjs';
import { Router } from '@angular/router';
import { orderTO } from '../../shared/types/orderTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component'
import { equipmentTO } from '../../shared/types/equipmentTO';
import { productTO } from '../../shared/types/productTO';

// Default product used in the order when no product is selected
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
  selector: 'app-create-order-from-existing',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonComponent],
  templateUrl: './create-order-from-existing.component.html',
  styleUrl: './create-order-from-existing.component.css'
})
export class CreateOrderFromExistingComponent implements OnInit {
  
  // Order object initialized with default values
  order: orderTO = {
    orderNumber: "",
    name: "",
    description: "",
    productAfter: defaultProduct,
    productBefore: defaultProduct,
    equipment: [], 
    workflows: []
  };

  selectedOrder: orderTO | null = null; // Holds the selected order from suggestions
  suggestions: orderTO[] = []; // Store fetched orders as suggestions
  createDisabled: boolean = true; // Controls whether the Create Order button is enabled
  createBtnLabel: string = 'Create Order'; // Label for the Create Order button
  orderNumberExists: boolean = false; // Tracks if the order number already exists
  equipmentList: equipmentTO[] = []; // List of available equipment
  selectedEquipment: equipmentTO[] = []; // Selected equipment for the order
  productList: productTO[] = []; // List of available products
  selectedProductBefore: productTO | null = null; // Product selected before
  selectedProductAfter: productTO | null = null; // Product selected after

  constructor(
    private router: Router, // Router for navigation
    private backendCommunicationService: BackendCommunicationService // Service for backend communication
  ) { }

  // Fetch all equipment data from the backend
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
          alert('Error fetching equipment data');
          return of([]); // Return empty array if an error occurs
        })
      )
      .subscribe((equipment: equipmentTO[]) => this.equipmentList = equipment); // Populate the equipment list
  }

  // Update the selected equipment for the order
  onEquipmentChange(selectedEquipments: equipmentTO[]) {
    this.order.equipment = [...selectedEquipments]; // Set selected equipment to the order's equipment
  }

  // Fetch all product data from the backend
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
          alert('Error fetching product data');
          return of([]); // Return empty array if an error occurs
        })
      )
      .subscribe((products: productTO[]) => this.productList = products); // Populate the product list
  }

  // Update the product selected for the "Before" state in the order
  onProductBeforeChange(selectedProductBefore: productTO) {
    this.order.productBefore = selectedProductBefore; // Set the selected "before" product to the order
  }

  // Update the product selected for the "After" state in the order
  onProductAfterChange(selectedProductAfter: productTO) {
    this.order.productAfter = selectedProductAfter; // Set the selected "after" product to the order
  }

  // Initialize component data by fetching equipment, products, and order suggestions
  ngOnInit() {
    this.fetchEquipment(); // Fetch equipment
    this.fetchProducts(); // Fetch products
    this.loadSuggestions(); // Fetch order suggestions
  }

  // Fetch order suggestions from the backend
  loadSuggestions() {
    this.backendCommunicationService.getEditorOrders().subscribe(
      (data) => {
        this.suggestions = data; // Populate the suggestions with fetched data
      },
      (error) => {
        alert('Error fetching order suggestions');
      }
    );
  }

  // Handle selection of a suggestion, copying it into the selectedOrder and resetting some fields
  selectSuggestion(suggestion: orderTO) {
    this.selectedOrder = { ...suggestion, orderNumber: "" }; // Reset order number for the selected order
    this.selectedEquipment = suggestion.equipment || []; // Set selected equipment from the suggestion
    this.selectedProductBefore = suggestion.productBefore || null; // Set selected "before" product from the suggestion
    this.selectedProductAfter = suggestion.productAfter || null; // Set selected "after" product from the suggestion
  }

  // Check if the order number is unique across all suggestions
  checkUniqueOrderNumber() {
    if (this.selectedOrder) {
      // Check if the order number already exists in the suggestions
      this.orderNumberExists = this.suggestions.some(
        (suggestion) => suggestion.orderNumber === this.selectedOrder!.orderNumber
      );

      // Enable the button only if the order number is unique and all other fields are filled
      this.createDisabled = this.orderNumberExists || !(
        this.selectedOrder.orderNumber &&
        this.selectedOrder.name &&
        this.selectedOrder.description &&
        this.order.equipment.length > 0 && // Ensure at least one equipment is selected
        this.order.productBefore && 
        this.order.productAfter
      );
    }
  }

  // Create the order if the form is valid and the order number is unique
  createOrder(event: MouseEvent) {
    if (event.type === 'click' && !this.orderNumberExists) {
      this.backendCommunicationService.createOrder(this.selectedOrder!)
        .pipe(
          catchError((error) => {
            alert('Failed to create order. Please try again.');
            return of(null); // Return null to continue observable flow
          })
        )
        .subscribe({
          next: (response: any) => {
            if (response && response.id) {
              alert('Order created successfully!');

              // Redirect to the newly created order after a short delay
              setTimeout(() => {
                this.router.navigate(['/editor/orders', response.id]); // Navigate to the created order
              }, 1000); // 1-second delay
            }
          },
          error: () => {
            alert('Unexpected error occurred during order creation.');
          }
        });
    } else {
      alert('Please enter a unique order number.'); // Show alert if order number is not unique
    }
  }
}
