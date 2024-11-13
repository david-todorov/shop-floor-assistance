import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from '../../shared/component-elements/task-tab/task-tab.component';

@Component({
  selector: 'app-operator-view-workflow',
  standalone: true,
  imports: [RouterModule,  
    WorkflowAccordionComponent,TaskTabComponent 
  ],
  templateUrl: './operator-view-workflow.component.html',
  styleUrl: './operator-view-workflow.component.css'
})
export class OperatorViewWorkflowComponent {
  
 constructor(private backendCommunicationService: BackendCommunicationService,
    private route: ActivatedRoute,) {
    this.route.params.subscribe(params => {
      const orderId = params['id'];
      this.backendCommunicationService.getOperatorOrder(orderId).pipe(
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


