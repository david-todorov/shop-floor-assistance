import { Component, OnInit } from '@angular/core';
import { OrderTableComponent } from '../../shared/component-elements/order-table/order-table.component';
import { orderTO } from '../../shared/types/orderTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { Router, RouterLink } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';
import { OperatorExecutionTO } from '../../shared/types/OperatorExecutionTO';

@Component({
  selector: 'app-operator',
  standalone: true,
  imports: [
    OrderTableComponent,
    ButtonComponent,
    RouterLink
  ],
  templateUrl: './operator.component.html',
  styleUrl: './operator.component.css'
})
export class OperatorComponent implements OnInit {

  // Label for the button displayed on the UI
  btnLabel: string = 'Start Order';

  // Flag to control whether the button is disabled
  disabledd: boolean = false;

  // Currently selected order
  order!: orderTO;

  // List of loaded orders fetched from the backend
  loadedOrders!: orderTO[];

  // Tracks if the execution has started
  executionStarted: boolean = false;

  // Tracks the current execution data
  execution!: OperatorExecutionTO | null;

  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) {}

  ngOnInit(): void {
    // Fetch all orders from the backend on component initialization
    this.backendCommunicationService.getAllOrders().pipe(
      // Handle errors when fetching orders
      catchError((err) => {
        console.error('Error fetching orders:', err); // Log the error
        return of([]); // Return an empty array if an error occurs
      })
    ).subscribe((response: orderTO[]) => {
      const updatedOrders = response.map(order => {
        // Fetch forecast data for each order
        this.backendCommunicationService.getForecast(order.id!).pipe(
          catchError((err) => {
            console.error(`Error fetching forecast for order ${order.id}:`, err); // Log the error
            return of({ total_time_required: null }); // Default to null if there's an error
          })
        ).subscribe((forecast) => {
          // Example of assigning forecast data to order 
          // order.forecast = forecast;
        });
        return order;
      });
      // Update the component's orders list
      this.loadedOrders = updatedOrders;
    });
  }

  /**
   * Handles selection of an order from the UI
   * @param $event - Event containing the selected order
   */
  orderSelected($event: any) {
    this.order = $event;
  }

  /**
   * Handles the button click to resolve the order
   * @param $event - Click event from the button
   */
  resolveButtonClick($event: any) {
    if ($event.type === 'click') {
      if (!this.order) {
        alert('You must specify an order!');
        return;
      }

      // Start the selected order by posting to the backend
      this.backendCommunicationService.startOrder(this.order.id!, this.order)
        .subscribe({
          next: (response: OperatorExecutionTO) => {
            // Navigate to the order details page with the execution ID as a query parameter
            this.router.navigate(['/operator/orders', this.order.id], {
              queryParams: { executionId: response.id }
            });
          },
          error: (err) => {
            console.error('Error starting the order:', err); // Log the error
            alert('Failed to start the order. Please check the details.');
          }
        });
    }
  }
}
