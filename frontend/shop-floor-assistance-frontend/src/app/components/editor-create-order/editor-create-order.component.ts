import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { CommonModule } from '@angular/common';
import { orderTO } from '../../types/orderTO'; // Adjust the import path as needed

@Component({
  selector: 'app-editor-create-order',
  standalone: true,
  imports: [FormsModule, ButtonComponent, CommonModule],
  templateUrl: './editor-create-order.component.html',
  styleUrl: './editor-create-order.component.css'
})
export class EditorCreateOrderComponent implements OnInit{
  @Input() order: any;

  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  ngOnInit() {
    if (this.order) {
      console.log("ngOnInit - Received order data from parent:", this.order);
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['order'] && changes['order'].currentValue) {
      console.log("ngOnChanges - Received order data from parent:", this.order);
    }
  }

  // Method to add a new workflow
  addNewWorkflow() {
    const newWorkflow = {
      workflowNumber: (this.order.workflows.length + 1).toString(),
      name: 'New Workflow', // Default name for the new workflow
      tasks: [] // Initialize with an empty array for tasks
    };

    this.order.workflows.push(newWorkflow);
    console.log('New workflow added:', newWorkflow);
  }

 /*  addNewTask() {
    if (this.selectedWorkflowIndex !== null && this.order.workflows[this.selectedWorkflowIndex]) {
      const newTask = {
        taskNumber: (this.order.workflows[this.selectedWorkflowIndex].tasks.length + 1).toString(), // Unique identifier for the task
        name: 'New Task', // Assign default name for the new task
        workflows: [], // Initialize as an empty array for workflows if it's required
        items: [] // Initialize with an empty array of items
      };
      this.order.workflows[this.selectedWorkflowIndex].tasks.push(newTask);
      console.log('New task added:', newTask);
    } else {
      console.warn('No workflow selected to add a task');
    }
  } */

  submitToBackend() {
    this.backendCommunicationService.createOrder(this.order).subscribe({
      next: (response) => {
        console.log('Order submitted successfully:', response);
        alert('Order created successfully!');
      },
      error: (error) => {
        console.error('Error submitting order:', error);
        alert('Failed to create order. Please try again.');
      }
    });
  }
}

/* 
  createDisabled: boolean = true;
  createBtnLabel: string = 'Save Order';

  // State for UI changes
  orderState = {
    buttonIcon: 'save',
    buttonLabel: 'Save Order',
    isSaved: false
  };

  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  saveOrder(event: MouseEvent) {
    if (
      event.type === 'click' &&
      this.order.orderNumber && this.order.name && this.order.equipment && this.order.productBefore
      && this.order.productAfter && this.order.workflows 
    ) {
      this.backendCommunicationService.createOrder(this.order)
        .pipe(
          catchError((error) => {
            console.error('Error creating order:', error);
            alert('Failed to create order. Please try again.');
            return of(null);
          })
        )
        .subscribe({
          next: (response) => {
            if (response) {
              console.log('Order created successfully:', response);

              this.orderState.buttonIcon = 'check_circle';
              this.orderState.buttonLabel = 'Saved';
              this.orderState.isSaved = true;

              alert('Order created successfully!');
              setTimeout(() => {
                this.router.navigateByUrl('/orders'); // Redirect to appropriate route
              }, 1000);
            }
          }
        });
    } else {
      console.error('All fields are required');
      alert('Please fill in all fields before saving.');
    }
  }

  checkFormCompletion() {
    this.createDisabled = !(this.order.name); // Update conditions based on required fields
  }
} */

