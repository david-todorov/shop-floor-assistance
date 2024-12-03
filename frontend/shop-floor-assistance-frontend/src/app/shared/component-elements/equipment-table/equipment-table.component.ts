import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { equipmentTO } from '../../types/equipmentTO';

@Component({
  selector: 'app-equipment-table', // Component selector for usage in templates
  standalone: true, // Indicates the component is standalone
  imports: [
    MatTableModule, // Material Table for displaying tabular data
    MatRadioModule, // Material Radio for selecting options
    MatPaginatorModule, // Material Paginator for table pagination
    MatSortModule // Material Sort for sorting table data
  ],
  templateUrl: './equipment-table.component.html', // Path to the HTML template
  styleUrl: './equipment-table.component.css' // Path to the CSS styles
})
export class EquipmentTableComponent implements OnInit {
  
  @Input() Equipment!: equipmentTO[]; // Input property for receiving equipment data
  @Output() onClick = new EventEmitter<any>(); // Output property to emit selected equipment

  dataSource!: MatTableDataSource<equipmentTO>; // Data source for the Material Table
  // @ViewChild(MatSort) sort!: MatSort; // Uncomment if sorting is implemented

  constructor() {}

  ngOnInit(): void {
    // Initialize the data source for the table
    this.dataSource = new MatTableDataSource<equipmentTO>();
    // Uncomment the following line if a sorting feature is implemented
    // this.dataSource.sort = this.sort;
  }

  /**
   * Lifecycle hook to handle changes to the Input property `Equipment`.
   * Updates the data source when new equipment data is provided.
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['Equipment'] && changes['Equipment'].currentValue) {
      this.dataSource.data = this.Equipment; // Update the data source with the new equipment data
    }
  }

  // Columns to be displayed in the Material Table
  displayedColumns: string[] = ['select', 'Equipment No.', 'Name', 'Description', 'Type'];

  ngAfterViewInit(): void {
    // Handle view initialization logic, e.g., initializing paginator/sorter
  }

  /**
   * Emits the selected equipment when a radio button is changed.
   * @param Equipment - The selected equipment
   */
  onRadioChange(Equipment: equipmentTO): void {
    this.onClick.emit(Equipment); // Emit the selected equipment through the Output property
  }

  /**
   * Applies a filter to the table based on user input.
   * @param $event - The input event containing the filter value
   */
  applyFilter($event: Event): void {
    const filterValue = ($event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase(); // Apply case-insensitive filtering
  }
}
