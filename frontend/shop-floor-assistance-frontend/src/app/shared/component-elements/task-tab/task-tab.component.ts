import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { orderTO } from '../../../types/orderTO';

import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-tab',
  standalone: true,
  imports: [
    MatIconModule,
    MatTabsModule,
    CommonModule,
  ],
  templateUrl: './task-tab.component.html',
  styleUrl: './task-tab.component.css'
})
export class TaskTabComponent implements OnChanges{


  @Input() workflowIndex!: number | null;
  @Input() orderUpdated!: orderTO;

  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onSelect = new EventEmitter<number | null>();

  selectedTaskIndex: number | null = 0;

  ngOnChanges(changes: SimpleChanges): void {
    if(this.workflowIndex!=null){
      console.log('updated order received in task-tab',this.orderUpdated,this.workflowIndex);
    }
  }
  




}
