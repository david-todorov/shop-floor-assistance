import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableModule } from '@angular/material/table';
import { orderTO } from '../../../types/orderTO';
import { sampleOrders } from '../../../types/dummyData';

@Component({
  selector: 'app-order-table',
  standalone: true,
  imports: [MatTableModule,
    MatRadioModule
  ],
  templateUrl: './order-table.component.html',
  styleUrl: './order-table.component.css',
})
export class OrderTableComponent implements OnInit{


  dataSource!:any;
  orders!: orderTO[];

  constructor(){}

  ngOnInit(): void {
    this.orders= sampleOrders; 
    this.dataSource = this.orders;
  }
  displayedColumns: string[] = ['select', 'Order No.', 'Name', 'Description', ];
  
  onRadioChange(order: orderTO) {
    console.log('Selected row data:', order);
  }
}
