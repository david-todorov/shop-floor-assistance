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
  selector: 'app-operator-task-tab',
  standalone: true,
 imports: [
    MatIconModule,
    MatTabsModule,
    CommonModule,
    MatDialogModule,
    EditTaskDialogComponent,
    ItemAccordionComponent
  ],
  templateUrl: './operator-task-tab.component.html',
  styleUrl: './operator-task-tab.component.css'
})
export class OperatorTaskTabComponent implements OnInit, OnChanges {

  @Input() workflowIndex!: number | null;
  @Input() orderUpdated!: orderTO;
  @Input() doneAll: boolean[] = [];
  // @Input() tasks: itemTO[] = [];

  @Output() orderUpdateFromTasks = new EventEmitter<orderTO>();
  @Output() onSelect = new EventEmitter<number | null>();

  selectedTaskIndex: number | null = 0;
  orderExists: boolean = false;

  taskFlowStates: workflowStates= {};

tasks!: taskTO[];

  constructor(public dialog: MatDialog,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // this.initializeWorkflowStates();
    if (this.workflowIndex === null || this.workflowIndex === undefined) {
      this.workflowIndex = 0;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['orderUpdated'] || changes['workflowIndex']) {
      this.initializeWorkflowStates();
      this.getTasksForSelectedWorkflow();
    }
  }

  getTasksForSelectedWorkflow() {

    if (this.workflowIndex !== null &&
      this.workflowIndex !== undefined &&
      this.orderUpdated &&
      this.orderUpdated.workflows) {
      this.tasks = this.orderUpdated.workflows[this.workflowIndex].tasks;
      this.tasks = [...this.orderUpdated.workflows[this.workflowIndex].tasks]; // Create a new array reference
      console.log('The selected index is:', this.selectedTaskIndex)
      console.log('The selected tasks are is:', this.tasks)
    } else {
      this.tasks = [];
    }
  }

  initializeWorkflowStates() {

  }

 
onTabChange(index: number): void {
  this.selectedTaskIndex = index;
  console.log('Selected tab index:', this.selectedTaskIndex);
}

updateItemsInOrder(event: itemTO[]) {
  if (this.workflowIndex != null && this.selectedTaskIndex != null) {
    const currentTask = this.orderUpdated.workflows[this.workflowIndex].tasks[this.selectedTaskIndex];
    
    console.log('Current Task ID:', currentTask.id); // Debugging line

    // Set taskId for each item only if currentTask.id is defined
    event.forEach(item => {
      // if (currentTask.id !== undefined) {
      //   item.taskId = currentTask.id; // Assign currentTask's ID to taskId
      // }
    });

    currentTask.items = event;
    this.orderUpdateFromTasks.emit(this.orderUpdated);
  }
}

resolveCheck(event: boolean, index: number) {
  this.doneAll[index]=event;
  const allTasksDone= this.doneAll.every(state => state);
  console.log('all tasks done', allTasksDone)
}

 // Method to emit order update
  updateOrder(order: any): void {
    this.orderUpdateFromTasks.emit(order);
  }
}


