import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { orderTO } from '../../../types/orderTO';

import { CommonModule } from '@angular/common';
import { ThemePalette } from '@angular/material/core';
import { workflowStates } from '../workflowUI-state';

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
toggleEditMode(arg0: any,$event: MouseEvent) {
throw new Error('Method not implemented.');
}
deleteWorkflow(arg0: any,$event: MouseEvent) {
throw new Error('Method not implemented.');
}


  @Input() workflowIndex!: number | null;
  @Input() orderUpdated!: orderTO;
  @Input() doneAll: boolean= true;

  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onSelect = new EventEmitter<number | null>();

  selectedTaskIndex: number | null = 0;
  orderExists: boolean= false;

  taskFlowStates: workflowStates= {};
$index: any;

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
