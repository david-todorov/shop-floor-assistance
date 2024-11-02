import { Component, OnInit } from '@angular/core';
import { OrderTableComponent } from '../../shared/component-elements/order-table/order-table.component';
import { orderTO } from '../../types/orderTO';
import { sampleOrders } from '../../types/dummyData';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { Router, RouterLink } from '@angular/router';

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
  order!: orderTO;

  constructor(private router:Router){}

  ngOnInit(): void {
    this.loadedOrders= sampleOrders;
  }
  
  loadedOrders!: orderTO[];

  orderSelected($event: any) {
    this.order= $event
  }

  resolveButtonClick($event: any) {
    if($event.type==='click' && this.order !== null && this.order !== undefined){
      console.log(this.order);
      this.router.navigate(['/operator/', this.order.orderNumber ]);


    }
  }

}
