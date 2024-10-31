import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { orderTO } from '../../../types/orderTO';

import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';

@Component({
  selector: 'app-order-table',
  standalone: true,
  imports: [
    MatTableModule,
    MatRadioModule,
    MatPaginatorModule,
  ],
  templateUrl: './order-table.component.html',
  styleUrl: './order-table.component.css',
})
export class OrderTableComponent implements OnInit, AfterViewInit{


  @Input() orders!: orderTO[];
  @Output() onClick = new EventEmitter<any>();

  dataSource!:MatTableDataSource<orderTO>;

  constructor(){}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<orderTO>(this.orders);
  }

  displayedColumns: string[] = ['select', 'Order No.', 'Name', 'Description', ];

  
  // Paginator if required
  // @ViewChild(MatPaginator) paginator!: MatPaginator;
  ngAfterViewInit() {// this.dataSource.paginator = this.paginator;
    }

  onRadioChange(order: orderTO) {
    this.onClick.emit(order);
  }

  applyFilter($event: Event) {
      const filterValue = ($event.target as HTMLInputElement).value;
      this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}
