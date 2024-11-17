import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { catchError, map, of } from 'rxjs';
import { ButtonComponent } from "../../shared/component-elements/button/button.component";
import { equipmentTO } from '../../types/equipmentTO';
import { productTO } from '../../types/productTO';
import { orderTO } from '../../types/orderTO';

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
  order: orderTO = {
    orderNumber: "",
    name: "",
    description: "",
    productAfter: defaultProduct,
    productBefore: defaultProduct,
    equipment: [],
    workflows: []
  };

  equipmentList: equipmentTO[] = [];
  selectedEquipment: equipmentTO | null = null;
  productList: productTO[] = [];
  selectedProductBefore: productTO | null = null;
  selectedProductAfter: productTO | null = null;
  createDisabled: boolean = false;
  createBtnLabel: string = 'Create';

  orderState = {
    buttonIcon: 'save',
    buttonLabel: 'Save Order',
    isSaved: false
  };

  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  ngOnInit() {
    this.fetchEquipment();
    this.fetchProducts();
  }

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
      .subscribe((equipment: equipmentTO[]) => this.equipmentList = equipment);
  }

  onEquipmentChange(selectedEquipment: equipmentTO) {
    console.log('Selected Equipment:', selectedEquipment);
    this.order.equipment = [selectedEquipment];
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

  saveOrder(event: MouseEvent) {
    if (event.type === 'click' && this.isFormComplete()) {
      console.log('Order Payload:', this.order); // Debugging line
      this.backendCommunicationService.createOrder(this.order)
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

              // Update UI states
              this.orderState.buttonIcon = 'check_circle';
              this.orderState.buttonLabel = 'Saved';
              this.orderState.isSaved = true;

              alert('Order created successfully!');

              // Delay navigation
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
      alert('Please fill in all fields before saving.');
    }
  }

  isFormComplete(): boolean {
    return !!(
      this.order.orderNumber &&
      this.order.name &&
      this.order.description &&
      this.order.equipment &&
      this.order.productBefore &&
      this.order.productAfter
    );
  }

  checkFormCompletion() {
    this.createDisabled = !this.isFormComplete();
  }
}
