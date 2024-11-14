import { AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTabGroup, MatTabsModule } from '@angular/material/tabs';
import { orderTO } from '../../../types/orderTO';
import { CommonModule } from '@angular/common';
import { taskTO } from '../../../types/taskTO';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EditTaskDialogComponent } from '../edit-task-dialog/edit-task-dialog.component';
import { OperatoritemAccordionComponent } from '../operator-item-accordion/operator-item-accordion.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-operator-task-tab',
  standalone: true,
  imports: [
    MatIconModule,
    MatTabsModule,
    CommonModule,
    MatDialogModule,
    OperatoritemAccordionComponent,
    DragDropModule,
  ],
  templateUrl: './operator-task-tab.component.html',
  styleUrl: './operator-task-tab.component.css'
})
export class OperatorTaskTabComponent implements OnInit, OnChanges, AfterViewInit{

  @Input() order!: orderTO;
  @Input() workflowIndex!: number | null;
  
  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onWorkflowChange = new EventEmitter<number | null>();
  @Output() ontaskChange = new EventEmitter<number | null>();
  @ViewChild(MatTabGroup) tabGroup!: MatTabGroup;

  taskIndex: number | null = 0;
  orderExists: boolean= false;
  tasks!: taskTO[];
  

  constructor(public dialog: MatDialog,
              private cdr:ChangeDetectorRef,
              private snackBar: MatSnackBar
  ){}

  ngOnInit(): void {
    if (this.workflowIndex == null) {
      this.workflowIndex = 0;
      setTimeout(() => {
      this.setTabGroupIndex(0);
    })
    }
  }

  ngAfterViewInit(): void {
     setTimeout(() => {
      this.setTabGroupIndex(0);
    })
  }

  setTabGroupIndex(index: number): void {
    setTimeout(() => {
      this.tabGroup.selectedIndex = index;
      this.cdr.detectChanges(); 
    });
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


  updateItemsInOrder(event: orderTO){
    // if(this.workflowIndex!=null && this.taskIndex!=null){
      this.onOrderUpdate.emit(this.order);
    // }
  }

  onTaskSelected(taskIndex: number) {
    this.ontaskChange.emit(taskIndex);
  }


  showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }

  // ---Drag functions--------------------
  drop(event: CdkDragDrop<taskTO[]>){
    let previousIndex = parseInt(event.previousContainer.id.replace("task-",""));
    let currentIndex = parseInt(event.container.id.replace("task-",""));
    moveItemInArray(this.tasks, previousIndex, currentIndex);
    if(this.workflowIndex!=null){
      this.order.workflows[this.workflowIndex].tasks= [...this.tasks];
    }
    this.onOrderUpdate.emit(this.order);
  }

 getAllDragTabs(index:number){
    let taskList = []
    for(let i=0;i<this.tasks.length;i++){
      if(i!=index){
        taskList.push("task-"+i);
      }
    }
    return taskList;
  }
// ----------------------------------------------


}
