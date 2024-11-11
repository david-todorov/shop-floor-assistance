import { Component, Input, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { dummyOrder } from '../../types/dummyData';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from '../../shared/component-elements/task-tab/task-tab.component';

@Component({
  selector: 'app-editor-edit-order',
  standalone: true,
  imports: [
    WorkflowAccordionComponent,
    TaskTabComponent],
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

}
