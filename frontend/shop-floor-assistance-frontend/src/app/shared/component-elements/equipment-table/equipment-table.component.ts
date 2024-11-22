import { AfterViewInit, Component, EventEmitter, inject, Input, OnInit, Output, SimpleChanges, ViewChild, ViewEncapsulation } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { equipmentTO } from '../../types/equipmentTO';

@Component({
  selector: 'app-equipment-table',
  standalone: true,
  imports: [MatTableModule,
    MatRadioModule,
    MatPaginatorModule,
    MatSortModule,],
  templateUrl: './equipment-table.component.html',
  styleUrl: './equipment-table.component.css'
})
export class EquipmentTableComponent {
  @Input() Equipment!: equipmentTO[];
  @Output() onClick = new EventEmitter<any>();

  dataSource!:MatTableDataSource<equipmentTO>;
  // @ViewChild(MatSort) sort!: MatSort;

  constructor(){}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<equipmentTO>();
    // this.dataSource.sort = this.sort;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['Equipment'] && changes['Equipment'].currentValue) {
      console.log('Equipment loaded:', this.Equipment);
       this.dataSource.data = this.Equipment;
    }
  }

  displayedColumns: string[] = ['select', 'Equipment No.', 'Name', 'Description', 'Type' ];

  ngAfterViewInit() {
    }

  onRadioChange(Equipment: equipmentTO) {
    this.onClick.emit(Equipment);
  }

  applyFilter($event: Event) {
      const filterValue = ($event.target as HTMLInputElement).value;
      this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}
