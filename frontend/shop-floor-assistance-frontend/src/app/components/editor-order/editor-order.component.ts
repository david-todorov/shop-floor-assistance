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
export class EditorOrderComponent implements OnInit{
  btnLabel: string= 'Start Wizard';
  order!: orderTO;
  loadedOrders!: orderTO[];
  editDisabled: boolean= false;
  createDisabled: boolean= false;
  editBtnLabel: string= 'Edit Order';
  createBtnLabel: string= 'Create Order';

  constructor(private router:Router,
    private backendCommunicationService: BackendCommunicationService
  ){}

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
    this.order= $event
  }
  resolveButtonClick($event: any) {
    if($event.type==='click'){
      if(this.order === null || this.order === undefined){
        alert('You must specify an order!');
      }else{
      console.log(this.order);
      this.router.navigate(['/editor/orders', this.order.id ]);
      }
    }
     return;
  }
}
