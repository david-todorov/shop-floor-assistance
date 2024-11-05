import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { orderTO } from '../../../types/orderTO';
import { workflowTO } from '../../../types/workflowTO';
import { taskTO } from '../../../types/taskTO';
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
  selectedTaskIndex!: number | null;
  selectedWorkflowTasks!: taskTO[];


  ngOnChanges(changes: SimpleChanges): void {
    if(this.workflowIndex!=null){
      console.log(this.orderUpdated)
      console.log(this.orderUpdated.workflows[this.workflowIndex]);
    }
  }
  




}
