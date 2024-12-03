import { Component, OnInit } from '@angular/core';
import { OrderTableComponent } from '../../shared/component-elements/order-table/order-table.component';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { Router, RouterLink } from '@angular/router';
import { orderTO } from '../../shared/types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-editor-order',
  standalone: true,
  imports: [OrderTableComponent, ButtonComponent],
  templateUrl: './editor-order.component.html',
  styleUrl: './editor-order.component.css'
})
export class EditorOrderComponent implements OnInit {
  // Button labels
  btnLabel: string = 'Start Wizard';
  order!: orderTO;
  loadedOrders!: orderTO[];
  // Button states
  editDisabled: boolean = true;
  createDisabled: boolean = false;
  deleteDisabled: boolean = true;
  // Other button labels
  editBtnLabel: string = 'Edit Order';
  deleteBtnLabel: string = 'Delete Order';
  createBtnLabel: string = 'Create Order';

  constructor(
    private router: Router, // Injecting Router for navigation
    private backendCommunicationService: BackendCommunicationService // Service for backend communication
  ) { }

  ngOnInit(): void {
    // Fetch the orders when the component is initialized
    this.backendCommunicationService.getEditorOrders().pipe(
      catchError((err) => {
        console.error('Error fetching orders:', err);
        return of([]); // Return an empty array if there's an error
      })
    ).subscribe({
      next: (response: orderTO[]) => {
        this.loadedOrders = response; // Assign the API response to loadedOrders
      },
      error: (err) => {
        console.error('An error occurred while loading orders:', err);
      },
      complete: () => {
        // Completed order loading, no longer logging
      }
    });
  }

  // Handle order selection event
  orderSelected($event: any) {
    this.order = $event;
    // Enable or disable buttons based on the selected order's validity
    this.deleteDisabled = !this.order || !this.order.id;
    this.editDisabled = !this.order || !this.order.id;
  }

  // Handle button click events for create, edit, and delete actions
  resolveButtonClick($event: any, action: string): void {
    if ($event.type === 'click') {
      if (action === 'create') {
        // Navigate to the create order route
        this.router.navigateByUrl('/editor-orders/creation-option');
      } else if (action === 'edit') {
        if (!this.order || this.order.id === undefined) {
          alert('You must specify an order with a valid ID!');
        } else {
          // Navigate to the edit order route using the order ID
          this.router.navigate(['/editor/orders', this.order.id]);
        }
      } else if (action === 'delete') {
        // Call deleteOrder method if the delete button is clicked
        this.deleteOrder();
      }
    }
  }

  // Delete the selected order
  deleteOrder(): void {
    if (!this.order || !this.order.id) {
      alert('You must select an order with a valid ID to delete!');
      return;
    }

    // Confirm delete action
    const confirmDelete = confirm(
      `Are you sure you want to delete the order: ${this.order.name} (ID: ${this.order.id})?`
    );

    if (confirmDelete) {
      // Call backend service to delete the order
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
          alert('Failed to delete the order. Please try again.');
        },
      });
    }
  }

}
