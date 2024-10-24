import { CommonModule } from '@angular/common';
import { Component, ViewEncapsulation } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';
import { sampleOrders } from '../types/dummyData';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { orderTO } from '../types/orderTO';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { ButtonComponent } from '../components/button/button.component';
import { CardComponent } from '../components/card/card.component';

@Component({
  selector: 'app-editor-dashboard',
  standalone: true,
  imports: [RouterLink, RouterModule, CommonModule, 
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    FormsModule,
    MatButtonModule,
    ButtonComponent, 
    CardComponent],
  templateUrl: './editor-dashboard.component.html',
  styleUrl: './editor-dashboard.component.css',
  providers:[RouterLink, RouterModule],
  encapsulation: ViewEncapsulation.None
})
export class EditorDashboardComponent {
  constructor(private routerLink: RouterLink){}
  orders = sampleOrders;
  displayedColumns: string[] = ['orderNumber', 'name', 'shortDescription'];


}
