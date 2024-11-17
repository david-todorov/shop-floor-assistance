import { Component, OnInit } from '@angular/core';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { catchError, map, of } from 'rxjs';
import { Router } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component'
import { equipmentTO } from '../../types/equipmentTO';
import { productTO } from '../../types/productTO';

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
export class CreateOrderFromExistingComponent {
 order: orderTO = {
    orderNumber: "",
    name: "",
    description: "",
    productAfter: defaultProduct,
    productBefore: defaultProduct,
    equipment: [], 
    workflows: []
  };

  selectedOrder: orderTO | null = null;
  suggestions: orderTO[] = []; // Store fetched orders as suggestions
  createDisabled: boolean = true;
  createBtnLabel: string = 'Create Order';
  orderNumberExists: boolean = false;
  equipmentList: equipmentTO[] = [];
  selectedEquipment: equipmentTO[] = [];
  productList: productTO[] = [];
  selectedProductBefore: productTO | null = null;
  selectedProductAfter: productTO | null = null;

  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

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
          console.error('Error fetching equipment:', error);
          return of([]);
        })
      )
      .subscribe((equipment: equipmentTO[]) => {
        this.equipmentList = equipment;
       console.log("Equipment List:", this.equipmentList);
      });
  }

  onEquipmentChange(selectedEquipments: equipmentTO[]) {
    console.log('Selected Equipment:', selectedEquipments);
    this.order.equipment = [...selectedEquipments];
  }


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
          console.error('Error fetching products:', error);
          return of([]);
        })
      )
      .subscribe((products: productTO[]) => this.productList = products);
  }

  onProductBeforeChange(selectedProductBefore: productTO) {
    console.log('Selected Product Before:', this.selectedProductBefore);
    this.order.productBefore = selectedProductBefore;
  }

  onProductAfterChange(selectedProductAfter: productTO) {
    console.log('Selected Product After:', this.selectedProductAfter);
    this.order.productAfter = selectedProductAfter;
  }

  ngOnInit() {
    this.fetchEquipment();
    this.fetchProducts();
    this.loadSuggestions();
  }

  loadSuggestions() {
    // Fetch existing orders from the backend
    this.backendCommunicationService.getEditorOrders().subscribe(
      (data) => {
        this.suggestions = data;
      },
      (error) => {
        console.error('Error fetching order suggestions:', error);
      }
    );
  }

  selectSuggestion(suggestion: orderTO) {
    console.log("Order suggestion selected:", suggestion);
    this.selectedOrder = { ...suggestion, orderNumber: "" }; // Copy selected order but reset the order number
    this.selectedEquipment = suggestion.equipment || []; 
    this.selectedProductBefore = suggestion.productBefore || null; 
    this.selectedProductAfter = suggestion.productAfter || null; 
  }

  checkUniqueOrderNumber() {
    if (this.selectedOrder) {
      // Check if the order number exists in suggestions
      this.orderNumberExists = this.suggestions.some(
        (suggestion) => suggestion.orderNumber === this.selectedOrder!.orderNumber
      );

      // Enable the button only if all fields are filled and the order number is unique
      this.createDisabled = this.orderNumberExists || !(
        this.selectedOrder.orderNumber &&
        this.selectedOrder.name &&
        this.selectedOrder.description &&
        this.order.equipment &&
        this.order.productBefore &&
        this.order.productAfter
      );
    }
  }

  createOrder(event: MouseEvent) {
    if (event.type === 'click' && !this.orderNumberExists) {
      console.log("Payload being sent:", this.selectedOrder);
      this.backendCommunicationService.createOrder(this.selectedOrder!)
        .pipe(
          catchError((error) => {
            console.error('Error creating order:', error);
            alert('Failed to create order. Please try again.');
            return of(null); 
          })
        )
        .subscribe({
          next: (response: any) => {
            if (response && response.id) {
              console.log('Order created successfully:', response);
              alert('Order created successfully!');

              // Delay navigation to allow the user to see the message
              setTimeout(() => {
                this.router.navigate(['/editor/orders', response.id]);
              }, 1000); // 1-second delay
            }
          },
          error: () => {
            console.error('Unexpected error occurred during order creation.');
          }
        });
    } else {
      console.error('Order number must be unique');
      alert('Please enter a unique order number.');
    }
  }
}