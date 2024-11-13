import { Component, OnInit } from '@angular/core';
import { OrderTableComponent } from '../../shared/component-elements/order-table/order-table.component';
import { orderTO } from '../../types/orderTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { Router, RouterLink } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

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

  btnLabel: string= 'Start Wizard';
  disabledd: boolean= false;
  order!: orderTO;
  loadedOrders!: orderTO[];

  constructor(private router:Router,
    private backendCommunicationService: BackendCommunicationService
  ){}

  ngOnInit(): void {
    this.backendCommunicationService.getOperatorOrders().pipe(
      catchError((err)=>{
        console.log(err);
        return of(null);
      })
    ).subscribe({
      next:(response) => {
        console.log(response);
      },
      error: (err)=>{
        //for fallback method in complete
      },
      complete: ()=>{

        //this.loadedOrders= sampleOrders;// This is fallback, since apis do not function now. TAKE OUT IN PROD VERSION
        console.log('done', this.loadedOrders)
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
      this.router.navigate(['/operator/', this.order.orderNumber ]);
     
      }
    }
     return;
  }

}


