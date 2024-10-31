import { AfterViewInit, Component, EventEmitter, inject, Input, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { orderTO } from '../../../types/orderTO';

import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { LiveAnnouncer } from '@angular/cdk/a11y';

@Component({
  selector: 'app-order-table',
  standalone: true,
  imports: [
    MatTableModule,
    MatRadioModule,
    MatPaginatorModule,
    MatSortModule,
  ],
  templateUrl: './order-table.component.html',
  styleUrl: './order-table.component.css',
})
export class OrderTableComponent implements OnInit, AfterViewInit{

  @Input() orders!: orderTO[];
  @Output() onClick = new EventEmitter<any>();

  dataSource!:MatTableDataSource<orderTO>;
  // @ViewChild(MatSort) sort!: MatSort;

  constructor(){}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<orderTO>(this.orders);
    // this.dataSource.sort = this.sort;
    
  }

  displayedColumns: string[] = ['select', 'Order No.', 'Name', 'Description', ];

  
  // Paginator if required
  // @ViewChild(MatPaginator) paginator!: MatPaginator;
  ngAfterViewInit() {
    // this.dataSource.paginator = this.paginator;
    // this.dataSource.sort = this.sort;
    }

  onRadioChange(order: orderTO) {
    this.onClick.emit(order);
  }

  applyFilter($event: Event) {
      const filterValue = ($event.target as HTMLInputElement).value;
      this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}
