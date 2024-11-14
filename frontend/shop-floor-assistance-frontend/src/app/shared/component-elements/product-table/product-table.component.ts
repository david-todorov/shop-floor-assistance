
import { AfterViewInit, Component, EventEmitter, inject, Input, OnInit, Output, SimpleChanges, ViewChild, ViewEncapsulation } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { productTO } from '../../../types/productTO';


@Component({
  selector: 'app-product-table',
  standalone: true,
  imports: [MatTableModule,
    MatRadioModule,
    MatPaginatorModule,
    MatSortModule,],
  templateUrl: './product-table.component.html',
  styleUrl: './product-table.component.css'
})
export class ProductTableComponent {
  @Input() Product!: productTO[];
  @Output() onClick = new EventEmitter<any>();

  dataSource!:MatTableDataSource<productTO>;
  // @ViewChild(MatSort) sort!: MatSort;

  constructor(){}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<productTO>();
    // this.dataSource.sort = this.sort;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['Product'] && changes['Product'].currentValue) {
      console.log('Product loaded:', this.Product);
       this.dataSource.data = this.Product;
    }
  }

  displayedColumns: string[] = ['select', 'Product No.', 'Name', 'Type', 'Description', 'Language', 'Country', 'Package Size', 'Package Type' ];

  
  // Paginator if required
  // @ViewChild(MatPaginator) paginator!: MatPaginator;
  ngAfterViewInit() {
    // this.dataSource.paginator = this.paginator;
    // this.dataSource.sort = this.sort;
    }

  onRadioChange(product: productTO) {
    this.onClick.emit(product);
  }

  applyFilter($event: Event) {
      const filterValue = ($event.target as HTMLInputElement).value;
      this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}