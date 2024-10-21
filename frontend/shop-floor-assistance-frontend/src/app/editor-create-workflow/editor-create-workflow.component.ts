import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTabChangeEvent, MatTabsModule } from '@angular/material/tabs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { workflowTO } from '../types/workflowTO';
import { taskTO } from '../types/taskTO';
import { itemTO } from '../types/itemTO';
import { orderTO } from '../types/orderTO';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { EditTaskDialogComponent } from '../dialogs/edit-task-dialog/edit-task-dialog.component';
import { dummyOrder } from '../types/dummyData';


@Component({
  selector: 'app-editor-create-workflow',
  standalone: true,
  imports: [  MatFormFieldModule,
    MatInputModule,
    MatTabsModule,
    MatIconModule,
    MatDialogModule,
    FormsModule,
    CommonModule,],
  templateUrl: './editor-create-workflow.component.html',
  styleUrl: './editor-create-workflow.component.css'
})
export class EditorCreateWorkflowComponent implements OnInit {


  orderName: string = '';
  // workflows: string[] = [];
  // item: string = '';

  constructor(public dialog: MatDialog) {}
order: orderTO = {
    orderNumber: '',
    name: '',
    shortDescription: '',
    workflows: []
  };
  selectedWorkflowIndex: number | null = null;
  selectedIndex = 0;

  // State management for edit mode and description visibility
  workflowStates: { [key: number]: { editMode: boolean, showDescription: boolean } } = {};
  taskStates: { [key: number]: { editMode: boolean, showDescription: boolean } } = {};
  ngOnInit() {
    //this.loadOrder();
    this.loadorderFromDummyData();
    this.initializeWorkflowStates();
  }

    initializeWorkflowStates() {
    this.order.workflows.forEach((workflow: any, index: number) => {
      this.workflowStates[index] = { editMode: false, showDescription: false };
    });
  }
  
  get selectedWorkflowTasks(): taskTO[] {
    if (this.selectedWorkflowIndex !== null) {
      return this.order.workflows[this.selectedWorkflowIndex].tasks;
    }
    return [];
  }

  addWorkflow() {
    this.order.workflows.push({ name: '', description: '', tasks: [{ name: 'Task 1', description: '', items: [{ name: '', longDescription: '', timeRequired: null }] }] });
    this.workflowStates[this.order.workflows.length - 1] = { editMode: false, showDescription: false };
    this.saveOrder();
  }

  selectWorkflow(index: number) {
    this.selectedWorkflowIndex = index;
    this.selectedIndex = 0; // Reset the selected tab index
  }

  addTask() {
    if (this.selectedWorkflowIndex !== null) {
      const newIndex = this.order.workflows[this.selectedWorkflowIndex].tasks.length + 1;
      this.order.workflows[this.selectedWorkflowIndex].tasks.push({ name: `Task ${newIndex}`, description: '', items: [{ name: '', longDescription: '', timeRequired: null }] });
      this.selectedIndex = this.order.workflows[this.selectedWorkflowIndex].tasks.length - 1;
      this.saveOrder();
    }
  }

  addItem(taskIndex: number) {
    if (this.selectedWorkflowIndex !== null) {
      this.order.workflows[this.selectedWorkflowIndex].tasks[taskIndex].items.push({ name: '', longDescription: '', timeRequired: null });
      this.saveOrder();
    }
  }

  toggleEditMode(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.workflowStates[index].editMode = !this.workflowStates[index].editMode;
    this.saveOrder();
  }

  deleteWorkflow(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.order.workflows.splice(index, 1);
    delete this.workflowStates[index];
    this.saveOrder();
  }

  toggleDescription(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.workflowStates[index].showDescription = !this.workflowStates[index].showDescription;
    this.saveOrder();
  }

  saveOrder() {
    localStorage.setItem('order', JSON.stringify(this.order));
    localStorage.setItem('workflowStates', JSON.stringify(this.workflowStates));
  }

  loadOrder() {
    const savedOrder = localStorage.getItem('order');
    const savedWorkflowStates = localStorage.getItem('workflowStates');
    if (savedOrder) {
      this.order = JSON.parse(savedOrder);
    }
    if (savedWorkflowStates) {
      this.workflowStates = JSON.parse(savedWorkflowStates);
    }
  }

  loadorderFromDummyData(){
    this.order= dummyOrder;
    this.saveOrder();
    this.loadOrder();
  }

  deleteTask(index: number, event: MouseEvent) {
    event.stopPropagation();
    if (this.selectedWorkflowIndex !== null) {
      this.order.workflows[this.selectedWorkflowIndex].tasks.splice(index, 1);
      delete this.taskStates[index];
      this.saveOrder();
    }
  }

    openEditDialog(index: number, isEditMode: boolean) {
    const dialogRef = this.dialog.open(EditTaskDialogComponent, {
      width: '400px',
      data: { task: { ...this.selectedWorkflowTasks[index] }, isEditMode }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.selectedWorkflowTasks[index] = result;
        this.saveOrder();
      }
    });
  }
}


