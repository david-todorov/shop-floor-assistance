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
import { SuggestionsComponent } from '../suggestions/suggestions.component';
import { itemCheckStatuses } from '../../shared/component-elements/workflowUI-state';


@Component({
  selector: 'app-editor-edit-order',
  standalone: true,
  imports: [WorkflowAccordionComponent, 
    TaskTabComponent, ButtonComponent, SuggestionsComponent],
  templateUrl: './editor-edit-order.component.html',
  styleUrl: './editor-edit-order.component.css'
})
export class EditorEditOrderComponent {
  
  isEditorMode=false;





  btnLabelAddWorkflow: string= 'Add Workflow';
  orderId!:number;
  productAfter!:productTO;

  order!: orderTO;
  selectedWorkflowIndex!: number | null;

  checkedOrder!: orderTO;
  checkStatuses!: itemCheckStatuses;

  constructor(private backendCommunicationService: BackendCommunicationService,
    private route: ActivatedRoute,
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
            this.filterOrder(this.initializeCheckStatuses());
            console.log('checkedOrder on initial loading', this.checkedOrder)
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
              name: 'New Item',
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

  onItemsCheckReceived(checkStatuses: itemCheckStatuses): void {
    this.filterOrder(checkStatuses);
  }

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

  filterOrder(checkStatuses: itemCheckStatuses){
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
