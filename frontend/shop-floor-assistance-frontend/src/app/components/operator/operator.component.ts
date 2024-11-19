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
  imports: [OrderTableComponent,
    ButtonComponent,RouterLink
  ],
  templateUrl: './operator.component.html',
  styleUrl: './operator.component.css'
})
export class OperatorComponent implements OnInit{

  btnLabel: string= 'Start Order';
  disabledd: boolean= false;
  order!: orderTO;
  loadedOrders!: orderTO[];

executionStarted: boolean = false; // Tracks if the execution has started
execution!: OperatorExecutionTO | null; // Tracks the current execution

  constructor(private router:Router,
    private backendCommunicationService: BackendCommunicationService
  ){}

ngOnInit(): void {
    this.backendCommunicationService.getAllOrders().pipe(
        catchError((err) => {
            console.error('Error fetching orders:', err);
            return of([]); // Return an empty array if there's an error
        })
    ).subscribe((response: orderTO[]) => {
        const updatedOrders = response.map(order => {
            this.backendCommunicationService.getForecast(order.id!).pipe(
                catchError((err) => {
                    console.error(`Error fetching forecast for order ${order.id}:`, err);
                    return of({ total_time_required: null }); // Default to null if there's an error
                })
            ).subscribe((forecast) => {
                // order.forecast = forecast; // Assign forecast data
            });
            return order;
        });
        this.loadedOrders = updatedOrders; // Assign updated orders to loadedOrders
    });
}


  
  orderSelected($event: any) {
    this.order= $event
  }

resolveButtonClick($event: any) {
  if ($event.type === 'click') {
    if (!this.order) {
      alert('You must specify an order!');
      return;
    }

    // Post the order to the backend
    this.backendCommunicationService.startOrder(this.order.id!, this.order)
      .subscribe({
        next: (response: OperatorExecutionTO) => {
          console.log('Order started successfully:', response);

          // Pass the execution ID to the next window using query parameters
          this.router.navigate(['/operator/orders', this.order.id], {
            queryParams: { executionId: response.id }
          });
        },
        error: (err) => {
          console.error('Error starting the order:', err);
          alert('Failed to start the order. Please check the details.');
        }
      });
  }
}



}

  




