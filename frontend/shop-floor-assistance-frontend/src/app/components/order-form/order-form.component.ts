import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { catchError, map, of } from 'rxjs';
import { ButtonComponent } from "../../shared/component-elements/button/button.component";
import { equipmentTO } from '../../types/equipmentTO';
import { productTO } from '../../types/productTO';
import { EditorCreateOrderComponent } from '../editor-create-order/editor-create-order.component';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [FormsModule, CommonModule, ButtonComponent, EditorCreateOrderComponent],
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent implements OnInit {
  order = {
    orderNumber: "",
    name: "",
    description: "",
    equipment: [],
    productAfter: [],
    productBefore: [],
    workflows: []
  };

  equipmentList: equipmentTO[] = [];
  productList: productTO[] = [];
  nextDisabled: boolean = false;
  nextBtnLabel: string = 'Next';

  @Output() orderSubmitted = new EventEmitter<any>();

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

  fetchProducts() {
    this.backendCommunicationService.getAllEditorProduct()
      .pipe(
        map((response: any[]) => response.map(item => ({
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

  submitOrderData() {
    if (this.order.name && this.order.equipment && this.order.productBefore
      && this.order.productAfter && this.order.orderNumber) {
      console.log('Order Data:', this.order);
      this.router.navigateByUrl('/editor-orders/create', { state: { order: this.order } });
    } else {
      alert('Please fill in all fields before proceeding.');
    }
  }
}
