import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { orderTO } from '../../../types/orderTO';

import { CommonModule } from '@angular/common';
import { ThemePalette } from '@angular/material/core';
import { workflowStates } from '../workflowUI-state';
import { taskTO } from '../../../types/taskTO';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EditTaskDialogComponent } from '../edit-task-dialog/edit-task-dialog.component';

@Component({
  selector: 'app-task-tab',
  standalone: true,
  imports: [
    MatIconModule,
    MatTabsModule,
    CommonModule,
    MatDialogModule,
    EditTaskDialogComponent
  ],
  templateUrl: './task-tab.component.html',
  styleUrl: './task-tab.component.css'
})
export class TaskTabComponent implements OnInit, OnChanges{


  @Input() workflowIndex!: number | null;
  @Input() orderUpdated!: orderTO;
  @Input() doneAll: boolean= true;

  @Output() orderUpdateFromTasks = new EventEmitter<orderTO>();
  // @Output() onSelect = new EventEmitter<number | null>();

  selectedTaskIndex: number | null = 0;
  orderExists: boolean= false;

  taskFlowStates: workflowStates= {};

  tasks!: taskTO[];

  constructor(public dialog: MatDialog){}
  
  ngOnInit(): void {
    // this.initializeWorkflowStates();
    if (this.workflowIndex === null || this.workflowIndex === undefined) {
      this.workflowIndex = 0;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['orderUpdated'] || changes['workflowIndex']) {
      // this.initializeWorkflowStates();
      this.getTasksForSelectedWorkflow();
    }
  }

  getTasksForSelectedWorkflow() {
    if(this.workflowIndex !== null && 
      this.workflowIndex !== undefined &&  
      this.orderUpdated && 
      this.orderUpdated.workflows) {
      this.tasks=  this.orderUpdated.workflows[this.workflowIndex].tasks;
    }
  }

  initializeWorkflowStates() {

  }

  deleteWorkflow(index: any,event: MouseEvent) {
    console.log('delete action', this.tasks[index].name )
    if (this.workflowIndex !== null) {
      event.stopPropagation();
      this.orderUpdated.workflows[this.workflowIndex].tasks.splice(index, 1);
      delete this.taskFlowStates[index];
      if (this.workflowIndex === index) {//no elements left case
        this.workflowIndex = null;
      } else if (this.workflowIndex > index) {
        this.workflowIndex--;
      }
      // this.initializeWorkflowStates();
    }
    // this.onSelect.emit(this.workflowIndex);
    this.orderUpdateFromTasks.emit(this.orderUpdated);
  }

  editTask(task: taskTO) {
        const dialogRef = this.dialog.open(EditTaskDialogComponent, {
      width: '750px',
      data: { ...task },
      panelClass: 'custom-dialog-container' 
    });

  }
}
