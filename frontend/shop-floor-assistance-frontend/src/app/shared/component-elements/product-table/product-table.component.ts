import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { productTO } from '../../types/productTO';

@Component({
  selector: 'app-product-table', // Selector for the product table component
  standalone: true, // Indicates that this component is standalone
  imports: [
    MatTableModule, // Material Table for tabular data display
    MatRadioModule, // Material Radio button for row selection
    MatPaginatorModule, // Material Paginator for pagination
    MatSortModule // Material Sort for column sorting
  ],
  templateUrl: './product-table.component.html', // HTML template path
  styleUrl: './product-table.component.css' // CSS styles path
})
export class ProductTableComponent implements OnInit, AfterViewInit {

  @Input() Product!: productTO[]; // Input property to receive an array of products
  @Output() onClick = new EventEmitter<any>(); // Output event emitter to notify row selection

  dataSource!: MatTableDataSource<productTO>; // Data source for the Material Table

  // Uncomment if sorting is implemented
  // @ViewChild(MatSort) sort!: MatSort;

  constructor() {}

  /**
   * Lifecycle hook called when the component is initialized.
   * Initializes the table's data source.
   */
  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<productTO>(); // Initialize the data source
    // Uncomment the following line to enable sorting
    // this.dataSource.sort = this.sort;
  }

  /**
   * Lifecycle hook called when input properties change.
   * Updates the table data source when `Product` input changes.
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['Product'] && changes['Product'].currentValue) {
      this.dataSource.data = this.Product; // Update the data source with new product data
    }
  }

  // Define columns to display in the table
  displayedColumns: string[] = [
    'select', 
    'Product No.', 
    'Name', 
    'Type', 
    'Description', 
    'Language', 
    'Country', 
    'Package Size', 
    'Package Type'
  ];

  /**
   * Lifecycle hook called after the view has been initialized.
   * Use this to initialize paginator or sorter if needed.
   */
  ngAfterViewInit(): void {
    // Uncomment and configure paginator and sorting if required
    // this.dataSource.paginator = this.paginator;
    // this.dataSource.sort = this.sort;
  }

  /**
   * Emits the selected product when the radio button is clicked.
   * @param product - The selected product
   */
  onRadioChange(product: productTO): void {
    this.onClick.emit(product); // Emit the selected product
  }

  /**
   * Filters the table data based on the user's input.
   * @param $event - The input event containing the filter value
   */
  applyFilter($event: Event): void {
    const filterValue = ($event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase(); // Apply case-insensitive filtering
  }
}
