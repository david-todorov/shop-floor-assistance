import { Component, Input, SimpleChanges } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from "../../shared/component-elements/task-tab/task-tab.component";
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { SuggestionsComponent } from '../../shared/component-elements/suggestions/suggestions.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { workflowTO } from '../../types/workflowTO';
import { productTO } from '../../types/productTO';

@Component({
  selector: 'app-editor-edit-order',
  standalone: true,
  imports: [WorkflowAccordionComponent, TaskTabComponent, ButtonComponent, SuggestionsComponent ],
  templateUrl: './editor-edit-order.component.html',
  styleUrl: './editor-edit-order.component.css'
})
export class EditorEditOrderComponent {
  btnLabelAddWorkflow: string= 'Add Workflow';
  saveBtnLabel: string = 'Save Order';
  orderId!:number;
  productAfter!:productTO;

  order!: orderTO;
  selectedWorkflowIndex!: number | null;

  constructor(private backendCommunicationService: BackendCommunicationService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar) {
    this.route.params.subscribe(params => {
      this.orderId = params['id'];
      this.backendCommunicationService.getEditorOrder(this.orderId).pipe(
        catchError((err) => {
          console.error(err);
          return of(null);
        })
      ).subscribe({
        next: (response) => {
          if (response) {
            this.order = response; // Assign the retrieved order data
            this.productAfter= this.order.productAfter;
            
            console.log('product after in edit page is: ', this.productAfter)
          } else {
            console.warn('No order found');
          }
        },
        complete: () => {
          console.log('Order retrieval complete:', this.order);
        }
      });
    });
  }

  updateOrder(order: orderTO) {
    this.order = { ...order };
    console.log('updates order received at parent', this.order)
  }


  saveOrder(event: MouseEvent): void {
    if (event.type === 'click' && this.order) {
      this.backendCommunicationService.updateEditorOrder(this.orderId, this.order)
        .pipe(
          catchError((error) => {
            console.error('Error updating order:', error);
            this.showSnackbar('Failed to save the order. Please try again.');
            return of(null);
          })
        )
        .subscribe((response) => {
          if (response) {
            this.showSnackbar('Order updated successfully!');
            // Redirect or update UI
            this.router.navigateByUrl('/editor/orders');
          }
        });
    }
  }

  onSelect(selectedWorkflow: number | null) {
    this.selectedWorkflowIndex = selectedWorkflow;
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

  addNewTask() {
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
  }

    resolveAddWorkflow(event: MouseEvent): void {
    event.stopPropagation();
    const newWorkflow: workflowTO = {
      name: 'New Workflow',
      description: 'New workflow: Description',
      tasks: [
        {
          name: 'New Task',
          description: 'New Task: Description',
          items: [
            {
              name: 'INew Item',
              description: 'New item: Description',
              timeRequired: null,
            }
          ]
        }
      ]
    };
    this.order.workflows.push(newWorkflow);
    this.order= {...this.order};
    console.log('updated order', this.order)
    this.showSnackbar('New workflow appended to the end of the workflows!');
  }

     showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }
}
