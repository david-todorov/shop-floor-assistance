import { Component, Input, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from "../../shared/component-elements/task-tab/task-tab.component";
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { workflowTO } from '../../types/workflowTO';
import { productTO } from '../../types/productTO';
import { CommonModule } from '@angular/common';
import { OperatorExecutionTO } from '../../types/OperatorExecutionTO';
import { Router ,RouterLink } from '@angular/router';

@Component({
  selector: 'app-operator-view-workflow',
  standalone: true,
  imports: [WorkflowAccordionComponent, TaskTabComponent, ButtonComponent,  CommonModule, RouterLink ],
  templateUrl: './operator-view-workflow.component.html',
  styleUrl: './operator-view-workflow.component.css'
})
export class OperatorViewWorkflowComponent {

  btnLabelFinish: string= 'Finish';
  orderId!:number;
  productAfter!:productTO;

  order!: orderTO;
  selectedWorkflowIndex!: number | null;
  executionStarted: boolean = false; // Tracks if an execution is in progress
  execution!: OperatorExecutionTO | null; // Tracks the current execution
  numericId: number | null = null;

  constructor(
  private backendCommunicationService: BackendCommunicationService,
  private route: ActivatedRoute,
  private snackBar: MatSnackBar,
  private router: Router // Add this line
) {
  this.route.params.subscribe(params => {
    this.orderId = params['id'];
    this.backendCommunicationService.getOperatorOrder(this.orderId).pipe(
      catchError((err) => {
        console.error(err);
        return of(null);
      })
    ).subscribe({
      next: (response) => {
        if (response) {
          this.order = response; // Assign the retrieved order data
          this.productAfter = this.order.productAfter;

          console.log('product after in edit page is: ', this.productAfter);
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


ngOnInit() {
  console.log('Component initialized with orderId:', this.orderId);

  // Fetch executionId from query parameters
  this.route.queryParams.subscribe(params => {
    const executionId = params['executionId'];
    if (executionId) {
      this.execution = { id: +executionId } as OperatorExecutionTO; // Create a basic execution object with the ID
      console.log('Execution ID received:', executionId);
    } else {
      console.warn('No execution ID found in query parameters.');
    }
  });
}




finishOrder(event: MouseEvent) {
    if (event.type === 'click') {
        if (!this.execution || !this.execution.id) {
            alert('No execution found to finish!');
            return;
        }

        this.backendCommunicationService.finishOrder(this.execution.id)
            .subscribe({
                next: (response: OperatorExecutionTO) => {
                    console.log('Execution finished successfully:', response);
                    this.execution = null; // Reset execution
                    this.executionStarted = false; // Reset state
                    this.showSnackbar('Order marked as finished successfully!');

                    // Navigate back to the orders page
                    this.router.navigateByUrl('/operator/orders');
                },
                error: (err) => {
                    console.error('Error finishing execution:', err);
                    alert('Failed to finish the execution. Please try again.');
                }
            });
    }
}


  updateOrder(order: orderTO) {
    this.order = { ...order };
    console.log('updates order received at parent', this.order)
  }

  onSelect(selectedWorkflow: number | null) {
    this.selectedWorkflowIndex = selectedWorkflow;
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


