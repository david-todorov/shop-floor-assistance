import { AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTabGroup, MatTabsModule } from '@angular/material/tabs';
import { orderTO } from '../../../types/orderTO';
import { CommonModule } from '@angular/common';
import { taskTO } from '../../../types/taskTO';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';
import { OperatoritemAccordionComponent } from '../operator-item-accordion/operator-item-accordion.component';

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
  tasks!: taskTO[];

  constructor(
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    if (this.workflowIndex == null) {
      this.workflowIndex = 0;
      setTimeout(() => {
        this.setTabGroupIndex(0);
      });
    }
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.setTabGroupIndex(0);
    });
  }

  setTabGroupIndex(index: number): void {
    setTimeout(() => {
      this.tabGroup.selectedIndex = index;
      this.cdr.detectChanges();
    });
  }

  // ngOnChanges(): void {
  //   if (changes['workflowIndex']) {
  //     this.taskIndex = 0;
  //     this.onWorkflowChange.emit(this.workflowIndex);
  //   }
  //   if (changes['workflowIndex'] || changes['order'] || changes['taskIndex']) {
  //     this.getTasksForSelectedWorkflow();
  //     console.log('Relevant changes detected:', changes);
  //   }
  // }
ngOnChanges(): void {
    if (this.workflowIndex !== null && this.order?.workflows) {
        this.tasks = this.order.workflows[this.workflowIndex]?.tasks || [];
        console.log('Tasks for workflow:', this.tasks);
    }
}



  getTasksForSelectedWorkflow(): void {
    if (this.workflowIndex != null && this.order.workflows) {
      this.tasks = [...this.order.workflows[this.workflowIndex].tasks];
    } else {
      this.tasks = [];
    }
  }

  onTaskSelected(taskIndex: number): void {
    this.ontaskChange.emit(taskIndex);
  }

  showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }
}