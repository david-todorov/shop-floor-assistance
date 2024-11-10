import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { orderTO } from '../../../types/orderTO';
import { CommonModule } from '@angular/common';
import { taskTO } from '../../../types/taskTO';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EditTaskDialogComponent } from '../edit-task-dialog/edit-task-dialog.component';
import { ItemAccordionComponent } from '../item-accordion/item-accordion.component';

@Component({
  selector: 'app-task-tab',
  standalone: true,
  imports: [
    MatIconModule,
    MatTabsModule,
    CommonModule,
    MatDialogModule,
    ItemAccordionComponent
  ],
  templateUrl: './task-tab.component.html',
  styleUrl: './task-tab.component.css'
})
export class TaskTabComponent implements OnInit, OnChanges{

  @Input() order!: orderTO;
  @Input() workflowIndex!: number | null;
  
  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onWorkflowChange = new EventEmitter<number | null>();
  @Output() ontaskChange = new EventEmitter<number | null>();

  taskIndex: number | null = 0;
  orderExists: boolean= false;
  tasks!: taskTO[];

  constructor(public dialog: MatDialog,
              private cdr:ChangeDetectorRef
  ){}
  
  ngOnInit(): void {
    if (this.workflowIndex == null) {
      this.workflowIndex = 0;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['workflowIndex']) {
      this.taskIndex = 0;
      this.onWorkflowChange.emit(this.workflowIndex);
    }
    if (changes['workflowIndex'] || changes['order'] || changes['taskIndex']) {
      this.getTasksForSelectedWorkflow();
      console.log('Relevant changes detected:', changes);
    }
  }

  getTasksForSelectedWorkflow() {
    if(this.workflowIndex != null && this.order.workflows) {
      this.tasks = [...this.order.workflows[this.workflowIndex].tasks];
    }else{
      this.tasks=[];
    }
  }

  deleteTasks(index: any,event: MouseEvent) {
      if (this.workflowIndex !== null) {
      event.stopPropagation();
      this.order.workflows[this.workflowIndex].tasks.splice(index, 1);
      if(index>=this.order.workflows[this.workflowIndex].tasks.length && this.taskIndex !=null){
        this.taskIndex-=1;
      }

      this.tasks = [...this.order.workflows[this.workflowIndex].tasks];
      this.onOrderUpdate.emit(this.order);
    }
  }

  editTask(task: taskTO) {
      const dialogRef = this.dialog.open(EditTaskDialogComponent, {
      width: '750px',
      data: { ...task },
      panelClass: 'custom-dialog-container' 
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && this.workflowIndex!=null && this.taskIndex!=null) {
        this.order.workflows[this.workflowIndex].tasks[this.taskIndex].name= result.taskname;
        this.order.workflows[this.workflowIndex].tasks[this.taskIndex].description= result.description;
      }
      this.onOrderUpdate.emit(this.order);
    });

  }

  updateItemsInOrder(event: orderTO){
    // if(this.workflowIndex!=null && this.taskIndex!=null){
      this.onOrderUpdate.emit(this.order);
    // }
  }

  onTaskSelected(taskIndex: number) {
    this.ontaskChange.emit(taskIndex);
  }

}
