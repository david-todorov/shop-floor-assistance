import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { OperatorWorkflowAccordionComponent } from '../../shared/component-elements/operator-workflow-accordion/operator-workflow-accordion.component';
import { OperatorTaskTabComponent } from '../../shared/component-elements/operator-task-tab/operator-task-tab.component';
import { workflowTO } from '../../types/workflowTO';

@Component({
  selector: 'app-operator-view-workflow',
  standalone: true,
  imports: [RouterModule,  
    OperatorWorkflowAccordionComponent,OperatorTaskTabComponent
  ],
  templateUrl: './operator-view-workflow.component.html',
  styleUrl: './operator-view-workflow.component.css'
})

export class OperatorViewWorkflowComponent implements OnInit {

  @Input() order!: orderTO;
  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  
  expandedPanels: boolean[] = [];
  selectedWorkflowIndex: number | null = null;



  
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

 ngOnInit(): void {
    this.initializeExpandedPanels();
  }

  // Initialize expanded panels based on the number of workflows
  private initializeExpandedPanels(): void {
    this.expandedPanels = new Array(this.order?.workflows?.length || 0).fill(false);
  }


  updateOrder(order: orderTO) : void{
    this.order = { ...order };
    console.log('updates order received at parent', this.order)
  }

  onSelect(selectedWorkflow: number | null): void {
    this.selectedWorkflowIndex = selectedWorkflow;
  }


}


