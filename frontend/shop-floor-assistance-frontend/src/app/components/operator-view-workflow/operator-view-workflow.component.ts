// Import statements for Angular components and other utilities
import { Component, Input, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { orderTO } from '../../shared/types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from "../../shared/component-elements/task-tab/task-tab.component";
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { workflowTO } from '../../shared/types/workflowTO';
import { productTO } from '../../shared/types/productTO';
import { CommonModule } from '@angular/common';
import { OperatorExecutionTO } from '../../shared/types/OperatorExecutionTO';
import { Router, RouterLink } from '@angular/router';
import { itemCheckStatuses } from '../../shared/types/workflowUI-state';

// Define the component metadata for OperatorViewWorkflowComponent
@Component({
  selector: 'app-operator-view-workflow',
  standalone: true,
  imports: [WorkflowAccordionComponent, TaskTabComponent, ButtonComponent, CommonModule],
  templateUrl: './operator-view-workflow.component.html',
  styleUrl: './operator-view-workflow.component.css'
})

export class OperatorViewWorkflowComponent {
  // Button labels for the UI
  btnLabelFinish: string = 'Finish';
  btnLabelAbort: string = 'Abort';

  // Properties to store the state of the component
  orderId!: number; // The ID of the current order
  productAfter!: productTO; // Data about the product after processing
  isEditorMode = false; // Indicates if the editor mode is active
  checkedOrder!: orderTO; // Stores the filtered order based on item checks
  order!: orderTO; // Stores the current order details
  selectedWorkflowIndex!: number | null; // Tracks the selected workflow index
  executionStarted: boolean = false; // Indicates if an execution is in progress
  execution!: OperatorExecutionTO | null; // The current execution object
  numericId: number | null = null; // Placeholder for numeric identifiers

  // Injecting required services
  constructor(
    private backendCommunicationService: BackendCommunicationService,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
    // Subscribe to route parameters to get the order ID
    this.route.params.subscribe(params => {
      this.orderId = params['id'];
      // Fetch order details using the backend service
      this.backendCommunicationService.getOperatorOrder(this.orderId).pipe(
        catchError((err) => {
          console.error('Error fetching order:', err);
          return of(null);
        })
      ).subscribe({
        next: (response) => {
          if (response) {
            this.order = response; // Assign the retrieved order
            this.productAfter = this.order.productAfter; // Extract product details
          }
        },
        complete: () => {
          // Execution completes here
        }
      });
    });
  }

  // Lifecycle hook for initialization logic
  ngOnInit() {
    // Subscribe to query parameters to fetch execution ID
    this.route.queryParams.subscribe(params => {
      const executionId = params['executionId'];
      if (executionId) {
        this.execution = { id: +executionId } as OperatorExecutionTO; // Initialize execution object
      }
    });
  }

  // Handles finishing the order
  finishOrder(event: MouseEvent) {
    if (event.type === 'click') {
      if (!this.execution || !this.execution.id) {
        alert('No execution found to finish!');
        return;
      }

      this.backendCommunicationService.finishOrder(this.execution.id)
        .subscribe({
          next: (response: OperatorExecutionTO) => {
            this.execution = null; // Reset execution
            this.executionStarted = false; // Reset state
            this.showSnackbar('Order marked as finished successfully!');
            this.router.navigateByUrl('/operator/orders'); // Navigate back to orders
          },
          error: (err) => {
            console.error('Error finishing execution:', err);
            alert('Failed to finish the execution. Please try again.');
          }
        });
    }
  }

  // Handles aborting the order
  abortOrder(event: MouseEvent) {
    if (event.type === 'click') {
      if (!this.execution || !this.execution.id) {
        alert('No execution found to abort!');
        return;
      }

      this.backendCommunicationService.abortOrderid(this.execution.id)
        .subscribe({
          next: (response: OperatorExecutionTO) => {
            this.execution = null; // Reset execution
            this.executionStarted = false; // Reset state
            this.showSnackbar('Order marked as aborted successfully!');
            this.router.navigateByUrl('/operator/orders'); // Navigate back to orders
          },
          error: (err) => {
            console.error('Error aborting execution:', err);
            alert('Failed to abort the execution. Please try again.');
          }
        });
    }
  }

  // Updates the order object
  updateOrder(order: orderTO) {
    this.order = { ...order };
  }

  // Handles workflow selection
  onSelect(selectedWorkflow: number | null) {
    this.selectedWorkflowIndex = selectedWorkflow;
  }

  // Handles checked items received from the UI
  onItemsCheckReceived(checkStatuses: itemCheckStatuses): void {
    this.filterOrder(checkStatuses);
  }

  // Filters the order based on checked item statuses
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

  // Displays a snackbar notification
  showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }
}
