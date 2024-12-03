import { AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { orderTO } from '../../types/orderTO';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { CommonModule } from '@angular/common';
import { BackendCommunicationService } from '../../../services/backend-communication.service';

@Component({
  selector: 'app-order-table', // Selector for the order table component
  standalone: true, // Indicates the component is standalone
  imports: [
    MatTableModule, // Material Table for tabular data
    MatRadioModule, // Material Radio buttons for selection
    MatPaginatorModule, // Material Paginator for pagination
    MatSortModule, // Material Sorting for table columns
    CommonModule // Common Angular module for core directives
  ],
  templateUrl: './order-table.component.html', // Path to the component's HTML template
  styleUrl: './order-table.component.css', // Path to the component's CSS styles
})
export class OrderTableComponent implements OnInit, AfterViewInit, OnChanges {

  @Input() orders!: orderTO[]; // Input property to receive a list of orders
  @Output() onClick = new EventEmitter<any>(); // Output event emitter to emit selected order

  dataSource!: MatTableDataSource<orderTO>; // Data source for the Material Table

  constructor(
    private backendCommunicationService: BackendCommunicationService // Service for backend API communication
  ) { }

  // Lifecycle hook called on component initialization
  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<orderTO>(); // Initialize data source for table
  }

  // Lifecycle hook called when input properties change
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['orders'] && changes['orders'].currentValue) {
      this.fetchForecastForOrders(); // Fetch forecast data when orders are updated
    }
  }

  /**
   * Fetches forecast data for each order and updates the table data source.
   */
  fetchForecastForOrders(): void {
    if (this.orders && this.orders.length > 0) {
      const updatedOrders: orderTO[] = [];

      this.orders.forEach(order => {
        this.backendCommunicationService.getForecast(order.id!).subscribe(
          (forecast) => {
            order.forecast = forecast; // Assign fetched forecast data to the order
            updatedOrders.push(order);

            // Update the data source once all orders are processed
            if (updatedOrders.length === this.orders.length) {
              this.dataSource.data = updatedOrders; // Update table with updated orders
            }
          },
          (err) => console.error(`Error fetching forecast for order ${order.id}:`, err) // Log error if forecast fetch fails
        );
      });
    }
  }

  // Columns to display in the table
  displayedColumns: string[] = ['select', 'Order No.', 'Name', 'Description', 'Equipment', 'Product Before', 'Product After', 'Forecast'];

  // Lifecycle hook called after the view has been initialized
  ngAfterViewInit(): void {
    // Uncomment and configure paginator and sorting if needed
    // this.dataSource.paginator = this.paginator;
    // this.dataSource.sort = this.sort;
  }

  /**
   * Handles selection change when a radio button is clicked.
   * @param order - The selected order
   */
  onRadioChange(order: orderTO): void {
    this.onClick.emit(order); // Emit the selected order
  }

  /**
   * Applies a filter to the table based on user input.
   * @param $event - The input event containing the filter value
   */
  applyFilter($event: Event): void {
    const filterValue = ($event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase(); // Filter table data based on input
  }
}
