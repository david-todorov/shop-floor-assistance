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
import { ItemAccordionComponent } from '../item-accordion/item-accordion.component';
import { itemTO } from '../../../types/itemTO';

@Component({
  selector: 'app-task-tab',
  standalone: true,
  imports: [
    MatIconModule,
    MatTabsModule,
    CommonModule,
    MatDialogModule,
    EditTaskDialogComponent,
    ItemAccordionComponent
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

  // taskFlowStates: workflowStates= {};

  tasks!: taskTO[];

  constructor(public dialog: MatDialog,
              private cdr:ChangeDetectorRef
  ){}
  
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
      // this.tasks=  this.orderUpdated.workflows[this.workflowIndex].tasks;
      this.tasks = [...this.orderUpdated.workflows[this.workflowIndex].tasks]; // Create a new array reference
          console.log('The selected index is:',this.selectedTaskIndex)
    console.log('The selected tasks are is:',this.tasks)
    }else{
      this.tasks=[];
    }
  }

  initializeWorkflowStates() {

  }

  deleteTasks(index: any,event: MouseEvent) {
      if (this.workflowIndex !== null) {
      event.stopPropagation();
      this.orderUpdated.workflows[this.workflowIndex].tasks.splice(index, 1);
      if(index>=this.orderUpdated.workflows[this.workflowIndex].tasks.length && this.selectedTaskIndex !=null){
        this.selectedTaskIndex-=1;
      }

      this.tasks = [...this.orderUpdated.workflows[this.workflowIndex].tasks]; // Create a new array reference
      this.orderUpdateFromTasks.emit(this.orderUpdated);
    }
  }

  editTask(task: taskTO) {
      const dialogRef = this.dialog.open(EditTaskDialogComponent, {
      width: '750px',
      data: { ...task },
      panelClass: 'custom-dialog-container' 
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && this.workflowIndex!=null && this.selectedTaskIndex!=null) {
        console.log(result)
        this.orderUpdated.workflows[this.workflowIndex].tasks[this.selectedTaskIndex].name= result.taskname;
        this.orderUpdated.workflows[this.workflowIndex].tasks[this.selectedTaskIndex].description= result.description;
      }
      this.orderUpdateFromTasks.emit(this.orderUpdated);
    });

  }

  onTabChange(index: number): void {
    this.selectedTaskIndex = index;
    console.log('Selected tab index:', this.selectedTaskIndex);
  }

  updateItemsInOrder(event: itemTO[]){
    if(this.workflowIndex!=null && this.selectedTaskIndex!=null){
      this.orderUpdated.workflows[this.workflowIndex].tasks[this.selectedTaskIndex].items= event;
      this.orderUpdateFromTasks.emit(this.orderUpdated);
    }
  }
}
