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
  selectedTaskIndex = 0;
  selectedItemIndex= 0;

  // State management for edit mode and description visibility
  workflowStates: { [key: number]: { editMode: boolean, showDescription: boolean } } = {};
  taskStates: { [key: number]: { editMode: boolean, showDescription: boolean } } = {};
  itemStates: { [key: number]: { editMode: boolean, showDescription: boolean } } = {};

  ngOnInit() {
    //this.loadOrder();
    this.loadorderFromDummyData();
    this.initializeWorkflowStates();
    this.initializeItemStates();
  }

  initializeWorkflowStates() {
    this.order.workflows.forEach((workflow: any, index: number) => {
      this.workflowStates[index] = { editMode: false, showDescription: false };
    });
  }

  initializeItemStates() {
    this.order.workflows.forEach((workflow: any, index1: number) => {
      workflow.tasks.forEach((task: any, index2: number)=>{
          this.itemStates[index2] = { editMode: false, showDescription: false };
        })
      } );
     
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
    this.selectedWorkflowIndex=this.order.workflows.length - 1;
    //this.saveOrder();
  }

  selectWorkflow(index: number) {
    this.selectedWorkflowIndex = index;
    this.selectedTaskIndex = 0; // Reset the selected tab index
    this.selectedItemIndex= 0;
  }

  addTask() {
    if (this.selectedWorkflowIndex !== null) {
      const newIndex = this.order.workflows[this.selectedWorkflowIndex].tasks.length + 1;
      this.order.workflows[this.selectedWorkflowIndex].tasks.push({ name: `Task ${newIndex}`, description: '', items: [{ name: '', longDescription: '', timeRequired: null }] });
      this.selectedTaskIndex = this.order.workflows[this.selectedWorkflowIndex].tasks.length - 1;
      //this.saveOrder();
    }
  }


  toggleEditMode(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.workflowStates[index].editMode = !this.workflowStates[index].editMode;
    //this.saveOrder();
  }

  deleteWorkflow(index: number, event: MouseEvent) {
    if (this.selectedWorkflowIndex !== null) {
      event.stopPropagation();
      this.order.workflows.splice(this.selectedWorkflowIndex, 1);
      delete this.workflowStates[this.selectedWorkflowIndex]; 
      this.workflowStates[this.order.workflows.length - 1] = { editMode: false,
         showDescription: false };
      const correctedIndex = this.order.workflows.length - 1;
      this.selectedWorkflowIndex=correctedIndex>=0?correctedIndex:null;
      //this.saveOrder();
    }
  }

  toggleDescription(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.workflowStates[index].showDescription = !this.workflowStates[index].showDescription;
    //this.saveOrder();
  }

  saveOrder() {
    localStorage.setItem('order', JSON.stringify(this.order));
    localStorage.setItem('workflowStates', JSON.stringify(this.workflowStates));
    localStorage.setItem('taskStates', JSON.stringify(this.taskStates));
    localStorage.setItem('itemStates', JSON.stringify(this.itemStates));
    console.log(this.order);
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
      //this.saveOrder();
    }
  }

    openEditDialog(index: number, isEditMode: boolean) {
    const dialogRef = this.dialog.open(EditTaskDialogComponent, {
      width: '400px',
      data: { task: { ...this.selectedWorkflowTasks[index] }, isEditMode}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.selectedWorkflowTasks[index] = result;
        //this.saveOrder();
      }
    });
  }



  // ---------------
  toggleItemDescription(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.itemStates[index].showDescription = !this.itemStates[index].showDescription;
    //this.saveOrder();
  }


  deleteItem(index: number, event: MouseEvent) {
      if (this.selectedWorkflowIndex !== null) {
        event.stopPropagation();
        this.order.workflows[this.selectedWorkflowIndex].tasks[this.selectedTaskIndex].items.splice(index, 1);
        delete this.itemStates[this.selectedItemIndex]; 
        this.itemStates[this.order.workflows[this.selectedWorkflowIndex].tasks[this.selectedTaskIndex].items.length - 1] = { editMode: false,
          showDescription: false };
        const correctedIndex = this.order.workflows[this.selectedWorkflowIndex].tasks[this.selectedTaskIndex].items.length - 1;
        this.selectedItemIndex=correctedIndex>=0?correctedIndex:0;
        //this.saveOrder();
      }
  }

  addItem() {
    if (this.selectedWorkflowIndex !== null) {
      this.order.workflows[this.selectedWorkflowIndex].tasks[this.selectedTaskIndex].items.push({ name: '', longDescription: '', timeRequired: null });
      //const ind= this.order.workflows[this.selectedWorkflowIndex].tasks[this.selectedTaskIndex].items.length - 1;
      this.itemStates[this.order.workflows[this.selectedWorkflowIndex].tasks[this.selectedTaskIndex].items.length - 1] = { editMode: false, showDescription: false };
      //this.saveOrder();
    }
  }

  toggleEditItemMode(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.itemStates[index].editMode = !this.itemStates[index].editMode;
    //this.saveOrder();
  }

}


