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
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { OperatorWorkflowAccordionComponent } from '../../shared/component-elements/operator-workflow-accordion/operator-workflow-accordion.component';
import { OperatorTaskTabComponent } from '../../shared/component-elements/operator-task-tab/operator-task-tab.component';

@Component({
  selector: 'app-operator-view-workflow',
  standalone: true,
  imports: [RouterModule, RouterLink,
    OperatorWorkflowAccordionComponent, OperatorTaskTabComponent
  ],
  templateUrl: './operator-view-workflow.component.html',
  styleUrl: './operator-view-workflow.component.css'
})

export class OperatorViewWorkflowComponent implements OnInit {

  order!: orderTO;
  selectedWorkflowIndex!: number | null;

  constructor(
    private backendCommunicationService: BackendCommunicationService,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const orderId = params['id'];
      this.backendCommunicationService.getOperatorOrder(orderId).pipe(
        catchError((err) => {
          console.error(err);
          this.snackBar.open("Failed to load order data.", "Close", { duration: 3000 });
          return of(null); // Continue the stream with a null value
        })
      ).subscribe((orderData) => {
        if (orderData) {
          this.order = orderData; // Assign data directly to `this.order`
        } else {
          console.warn("No order data available.");
        }
      });
    });
  }
}