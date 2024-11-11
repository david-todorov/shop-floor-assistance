import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTabGroup, MatTabsModule } from '@angular/material/tabs';
import { orderTO } from '../../../types/orderTO';
import { CommonModule } from '@angular/common';
import { taskTO } from '../../../types/taskTO';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EditTaskDialogComponent } from '../edit-task-dialog/edit-task-dialog.component';
import { ItemAccordionComponent } from '../item-accordion/item-accordion.component';
import { ButtonComponent } from '../button/button.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-task-tab',
  standalone: true,
  imports: [
    MatIconModule,
    MatTabsModule,
    CommonModule,
    MatDialogModule,
    ItemAccordionComponent,
    ButtonComponent
  ],
  templateUrl: './task-tab.component.html',
  styleUrl: './task-tab.component.css'
})
export class TaskTabComponent implements OnInit, OnChanges{

  resolveAddTAsk(event: any) {
    if(this.workflowIndex!=null){
      event.stopPropagation();
      const newTask: taskTO= {
        name: 'New Task', 
        description: 'Task Description', 
        items: [
          { name: 'New Item', 
            longDescription: 'Item Description', 
            timeRequired: null }
          ] 
        };
      this.order.workflows[this.workflowIndex].tasks.push(newTask);
      console.log(this.order)
      this.order= {...this.order};
      this.onOrderUpdate.emit(this.order);
      this.tabGroup.selectedIndex = 0;
      this.showSnackbar('New task appended to the end of the workflow!');

    }
  }

  showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }
       



  @Input() order!: orderTO;
  @Input() workflowIndex!: number | null;
  
  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onWorkflowChange = new EventEmitter<number | null>();
  @Output() ontaskChange = new EventEmitter<number | null>();
  @ViewChild(MatTabGroup) tabGroup!: MatTabGroup;

  taskIndex: number | null = 0;
  orderExists: boolean= false;
  tasks!: taskTO[];
  btnLabelAddTask: string= 'Add task';

  constructor(public dialog: MatDialog,
              private cdr:ChangeDetectorRef,
              private snackBar: MatSnackBar
  ){}
  
  ngOnInit(): void {
    if (this.workflowIndex == null) {
      this.workflowIndex = 0;
      this.tabGroup.selectedIndex = 0;
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
