import { Component, Input, SimpleChanges } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { orderTO } from '../../shared/types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from "../../shared/component-elements/task-tab/task-tab.component";
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { workflowTO } from '../../shared/types/workflowTO';
import { productTO } from '../../shared/types/productTO';
import { SuggestionsComponent } from '../suggestions/suggestions.component';
import { itemCheckStatuses } from '../../shared/types/workflowUI-state';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-editor-edit-order',
  standalone: true,
  imports: [WorkflowAccordionComponent, 
    TaskTabComponent, ButtonComponent, SuggestionsComponent, CommonModule],
  templateUrl: './editor-edit-order.component.html',
  styleUrl: './editor-edit-order.component.css'
})
export class EditorEditOrderComponent {
  
  // Editor mode flag
  isEditorMode = true;

  // Button labels
  btnLabelAddWorkflow: string = 'Add Workflow';
  saveBtnLabel: string = 'Save Order';

  // Order ID and related product
  orderId!: number;
  productAfter!: productTO;

  // The order to be edited and selected workflow index
  order!: orderTO;
  selectedWorkflowIndex!: number | null;

  // Checked order for workflow filtering
  checkedOrder!: orderTO;
  checkStatuses!: itemCheckStatuses;

  constructor(private backendCommunicationService: BackendCommunicationService,
              private route: ActivatedRoute,
              private router: Router,
              private snackBar: MatSnackBar) {
    // Fetch the order ID from the route parameters
    this.route.params.subscribe(params => {
      this.orderId = params['id'];

      // Fetch the order details from the backend
      this.backendCommunicationService.getEditorOrder(this.orderId).pipe(
        catchError((err) => {
          return of(null);
        })
      ).subscribe({
        next: (response) => {
          if (response) {
            this.order = response; // Assign the retrieved order data
            this.productAfter = this.order.productAfter;
            this.filterOrder(this.initializeCheckStatuses()); // Filter the order based on check statuses
          } else {
            console.warn('No order found');
          }
        },
        complete: () => {
        }
      });
    });
  }

  // Update the order object with new data
  updateOrder(order: orderTO) {
    this.order = { ...order };
  }

  // Save the updated order
  saveOrder(event: MouseEvent): void {
    if (event.type === 'click' && this.order) {
      this.backendCommunicationService.updateEditorOrder(this.orderId, this.order)
        .pipe(
          catchError((error) => {
            // Handle any errors that occur during the update
            this.showSnackbar('Failed to save the order. Please try again.');
            return of(null);
          })
        )
        .subscribe((response) => {
          if (response) {
            // Show success message and navigate
            this.showSnackbar('Order updated successfully!');
            this.router.navigateByUrl('/editor/orders');
          }
        });
    }
  }

  // Select a workflow for viewing or editing
  onSelect(selectedWorkflow: number | null) {
    this.selectedWorkflowIndex = selectedWorkflow;
  }

  // Add a new workflow to the order
  resolveAddWorkflow(event: MouseEvent): void {
    event.stopPropagation();

    // Define a new workflow with default tasks and items
    const newWorkflow: workflowTO = {
      name: 'New Workflow',
      description: 'New workflow: Description',
      tasks: [
        {
          name: 'New Task',
          description: 'New Task: Description',
          items: [
            {
              name: 'New Item',
              description: 'New item: Description',
              timeRequired: null,
            }
          ]
        }
      ]
    };

    // Append the new workflow to the order
    this.order.workflows.push(newWorkflow);
    this.order = { ...this.order };

    // Show a success message after adding the workflow
    this.showSnackbar('New workflow appended to the end of the workflows!');
  }

  // Show a snackbar message to the user
  showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }

  // Handle received item check statuses and filter the order accordingly
  onItemsCheckReceived(checkStatuses: itemCheckStatuses): void {
    this.filterOrder(checkStatuses);
  }

  // Initialize the check statuses for all items in the order
  initializeCheckStatuses(): itemCheckStatuses {
    this.checkStatuses = {};
    this.order.workflows.forEach((workflow, workflowIndex) => {
      this.checkStatuses[workflowIndex] = {};
      workflow.tasks.forEach((task, taskIndex) => {
        this.checkStatuses[workflowIndex][taskIndex] = {};
        task.items.forEach((item, itemIndex) => {
          this.checkStatuses[workflowIndex][taskIndex][itemIndex] = false;
        });
      });
    });
    return this.checkStatuses;
  }

  // Filter the order based on the check statuses
  filterOrder(checkStatuses: itemCheckStatuses) {
    this.checkedOrder = {
      ...this.order,
      workflows: this.order.workflows.map((workflow, workflowIndex) => {
        return {
          ...workflow,
          tasks: workflow.tasks.map((task, taskIndex) => {
            return {
              ...task,
              items: task.items.filter((item, itemIndex) => {
                return checkStatuses[workflowIndex][taskIndex][itemIndex];
              })
            };
          })
        };
      })
    };
  }
}
