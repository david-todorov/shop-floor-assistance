import { Component, EventEmitter, Input, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { ButtonComponent } from "../../shared/component-elements/button/button.component";
import { workflowTO } from '../../types/workflowTO';
import { itemUIStates } from '../../shared/component-elements/workflowUI-state';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterLink } from '@angular/router';
import { SuggestionsComponent } from '../../shared/component-elements/suggestions/suggestions.component';
import { CommonModule } from '@angular/common';
import { OperatorWorkflowAccordionComponent } from '../../shared/component-elements/operator-workflow-accordion/operator-workflow-accordion.component';
import { OperatorTaskTabComponent } from '../../shared/component-elements/operator-task-tab/operator-task-tab.component';



@Component({
  selector: 'app-operator-view-workflow',
  standalone: true,
  imports: [RouterModule, RouterLink,SuggestionsComponent,
    OperatorWorkflowAccordionComponent, OperatorTaskTabComponent
  ],
  templateUrl: './operator-view-workflow.component.html',
  styleUrl: './operator-view-workflow.component.css'
})

export class OperatorViewWorkflowComponent {

 order!: orderTO;
  selectedWorkflowIndex!: number | null;


  constructor(private backendCommunicationService: BackendCommunicationService,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar) {
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


  updateOrder(order: orderTO) {
    this.order = { ...order };
    console.log('updates order received at parent', this.order)
  }

  onSelect(selectedWorkflow: number | null) {
    console.log('Workflow selected in parent:', selectedWorkflow);
    this.selectedWorkflowIndex = selectedWorkflow;
}




     showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }

}