import { Component, OnInit } from '@angular/core';
import { OrderTableComponent } from '../../shared/component-elements/order-table/order-table.component';
import { orderTO } from '../../types/orderTO';
import { sampleOrders } from '../../types/dummyData';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';

@Component({
  selector: 'app-operator',
  standalone: true,
  imports: [OrderTableComponent,
    ButtonComponent
  ],
  templateUrl: './operator.component.html',
  styleUrl: './operator.component.css'
})
export class OperatorComponent implements OnInit{

  btnLabel: string= 'Start Wizard';
  order!: orderTO;

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
    }
  }

}
