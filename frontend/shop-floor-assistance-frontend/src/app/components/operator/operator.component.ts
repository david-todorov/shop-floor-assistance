import { Component, OnInit } from '@angular/core';
import { OrderTableComponent } from '../../shared/component-elements/order-table/order-table.component';
import { orderTO } from '../../types/orderTO';
import { sampleOrders } from '../../types/dummyData';

@Component({
  selector: 'app-operator',
  standalone: true,
  imports: [OrderTableComponent],
  templateUrl: './operator.component.html',
  styleUrl: './operator.component.css'
})
export class OperatorComponent implements OnInit{

  ngOnInit(): void {
    this.loadedOrders= sampleOrders;
  }
  
  loadedOrders!: orderTO[];


  resolveButtonClick($event: any) {

  console.log($event)

}

}
