import { Component, Input, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from "../../shared/component-elements/task-tab/task-tab.component";


@Component({
  selector: 'app-editor-edit-order',
  standalone: true,
  imports: [WorkflowAccordionComponent, TaskTabComponent],
  templateUrl: './editor-edit-order.component.html',
  styleUrl: './editor-edit-order.component.css'
})
export class EditorEditOrderComponent {

  constructor(private backendCommunicationService: BackendCommunicationService,
    private route: ActivatedRoute,) {
    this.route.params.subscribe(params => {
      const orderId = params['id'];
      this.backendCommunicationService.getEditorOrder(orderId).pipe(
        catchError((err) => {
          console.error(err);
          return of(null);
        })
      ).subscribe({
        next: (response) => {
          if (response) {
            this.order = response; // Assign the retrieved order data
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


  order!: orderTO;
  selectedWorkflowIndex!: number | null;


  updateOrder(order: orderTO) {
    this.order = { ...order };
    console.log('updates order received at parent', this.order)
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





}
