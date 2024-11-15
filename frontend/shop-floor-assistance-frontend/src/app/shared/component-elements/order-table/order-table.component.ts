import { AfterViewInit, Component, EventEmitter, inject, Input, OnInit, Output, SimpleChanges, ViewChild, ViewEncapsulation } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { orderTO } from '../../../types/orderTO';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-order-table',
  standalone: true,
  imports: [
    MatTableModule,
    MatRadioModule,
    MatPaginatorModule,
    MatSortModule,
    CommonModule
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
 
   this.dataSource = new MatTableDataSource<orderTO>();
    // this.dataSource.sort = this.sort;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['orders'] && changes['orders'].currentValue) {
      console.log('Orders loaded:', this.orders);

       // Calculate total time required for each order
      this.orders.forEach(order => {
        order.total_time_required = this.calculateForecastingTime(order);
      });
      
       this.dataSource.data = this.orders;
    }
  }

  displayedColumns: string[] = ['select', 'Order No.', 'Name', 'Description', 'Equipment', 'Product Before', 'Product After', 'Forecasting Time Estimate'];

  
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

  calculateForecastingTime(order: orderTO): number {
  return order.workflows.reduce((totalWorkflowTime, workflow) => {
    return totalWorkflowTime + workflow.tasks.reduce((totalTaskTime, task) => {
      return totalTaskTime + task.items.reduce((totalItemTime, item) => {
        return totalItemTime + (item.timeRequired || 0);
      }, 0);
    }, 0);
  }, 0);
}

}
