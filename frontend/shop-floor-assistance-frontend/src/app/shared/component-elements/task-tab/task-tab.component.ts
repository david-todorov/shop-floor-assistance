import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
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
export class TaskTabComponent implements OnInit, OnChanges{


  @Input() workflowIndex!: number | null;
  @Input() orderUpdated!: orderTO;

  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onSelect = new EventEmitter<number | null>();

  selectedTaskIndex: number | null = 0;
  orderExists: boolean= false;

  constructor(){}
  
  ngOnInit(): void {
    this.initializeWorkflowStates();
    if (this.workflowIndex === null || this.workflowIndex === undefined) {
      this.workflowIndex = 0;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['orderUpdated']) {
      this.initializeWorkflowStates();
    }
  }

  getTasksForSelectedWorkflow() {
    if (this.workflowIndex !== null && this.workflowIndex !== undefined &&  this.orderUpdated && this.orderUpdated.workflows) {
      return this.orderUpdated.workflows[this.workflowIndex].tasks;
    }
    return [];
  }


  initializeWorkflowStates() {
 
  }


}
