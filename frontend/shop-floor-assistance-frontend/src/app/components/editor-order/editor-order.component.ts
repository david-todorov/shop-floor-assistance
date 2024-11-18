import { Component, OnInit } from '@angular/core';
import { OrderTableComponent } from '../../shared/component-elements/order-table/order-table.component';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { Router, RouterLink } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-editor-order',
  standalone: true,
  imports: [OrderTableComponent,
    ButtonComponent,
  ],
  templateUrl: './editor-order.component.html',
  styleUrl: './editor-order.component.css'
})
export class EditorOrderComponent implements OnInit {
  btnLabel: string = 'Start Wizard';
  order!: orderTO;
  loadedOrders!: orderTO[];
  editDisabled: boolean = false;
  createDisabled: boolean = false;
  deleteDisabled: boolean = false;
  editBtnLabel: string = 'Edit Order';
  deleteBtnLabel: string = 'Delete Order';
  createBtnLabel: string = 'Create Order';

  constructor(private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  ngOnInit(): void {
    this.backendCommunicationService.getEditorOrders().pipe(
      catchError((err) => {
        console.error('Error fetching orders:', err);
        return of([]); // Return an empty array if there's an error
      })
    ).subscribe({
      next: (response: orderTO[]) => {
        this.loadedOrders = response; // Assign the API response to loadedOrders
        console.log('Orders loaded:', this.loadedOrders);
      },
      error: (err) => {
        console.error('An error occurred while loading orders:', err);
      },
      complete: () => {
        console.log('Order loading complete');
      }
    });
  }

  orderSelected($event: any) {
    this.order = $event;
    this.deleteDisabled = !this.order || !this.order.id;
  }

  resolveButtonClick($event: any, action: string): void {
    if ($event.type === 'click') {
      if (action === 'create') {
        // Directly navigate to the create equipment route without checking for equipment
        this.router.navigateByUrl('/editor-orders/creation-option');
      } else if (action === 'edit') {
        if (!this.order || this.order.id === undefined) {
          alert('You must specify an order with a valid ID!');
        } else {
          console.log('Selected order:', this.order);
          this.router.navigate(['/editor/orders', this.order.id]); // Use the numeric ID for navigation
        }
      } else if (action === 'delete') {
        this.deleteOrder();
      }
    }
  }

  deleteOrder(): void {
    if (!this.order || !this.order.id) {
      alert('You must select an order with a valid ID to delete!');
      return;
    }

    const confirmDelete = confirm(
      `Are you sure you want to delete the order: ${this.order.name} (ID: ${this.order.id})?`
    );

    if (confirmDelete) {
      this.backendCommunicationService.deleteEditorOrder(this.order.id).subscribe({
        next: () => {
          alert(`Order with ID ${this.order.id} deleted successfully.`);
          // Remove the deleted order from the loadedOrders array
          this.loadedOrders = this.loadedOrders.filter(
            (o) => o.id !== this.order.id
          );
          // Clear the selected order
          this.order = undefined!;
        },
        error: (error) => {
          console.error('Error deleting order:', error);
          alert('Failed to delete the order. Please try again.');
        },
      });
    }
  }
}