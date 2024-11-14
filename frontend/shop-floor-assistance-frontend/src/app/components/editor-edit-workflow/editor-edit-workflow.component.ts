import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { dummyOrder } from '../../types/dummyData';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from '../../shared/component-elements/task-tab/task-tab.component';
import { ButtonComponent } from "../../shared/component-elements/button/button.component";
import { workflowTO } from '../../types/workflowTO';
import { itemUIStates } from '../../shared/component-elements/workflowUI-state';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SuggestionsComponent } from '../../shared/component-elements/suggestions/suggestions.component';

@Component({
  selector: 'app-editor-edit-workflow',
  standalone: true,
  imports: [
    WorkflowAccordionComponent,
    TaskTabComponent,
    ButtonComponent,
    SuggestionsComponent
],
  templateUrl: './editor-edit-workflow.component.html',
  styleUrl: './editor-edit-workflow.component.css'
})
export class EditorEditWorkflowComponent implements OnInit {


  order!: orderTO;
  selectedWorkflowIndex!: number | null;
  btnLabelAddWorkflow: string= 'Add Workflow';

  itemUIStates: { [workflowIndex: number]: itemUIStates } = {};

  constructor(private backendCommunicationService:BackendCommunicationService,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar){
    this.route.params.subscribe(params => {
      const orderId= params['id'];
      this.backendCommunicationService.getEditorOrder(orderId).pipe(
        catchError((err)=>{
          console.log(err);
          return of(null);
        })
      ).subscribe({
        next:(response) => {
          console.log(response);
          // this.order=response;
        },
        error: (err)=>{
          //for fallback method in complete
        },
        complete: ()=>{
          this.order= dummyOrder;// This is fallback, since apis do not function now. TAKE OUT IN PROD VERSION
          console.log('done', this.order);

        }
      });
    });
  }


  ngOnInit(): void {}

  // @Input() set id(orderId: string){//received from previous page
  //       this.backendCommunicationService.getEditorOrder(orderId).pipe(
  //     catchError((err)=>{
  //       console.log(err);
  //       return of(null);
  //     })
  //   ).subscribe({
  //     next:(response) => {
  //       console.log(response);
  //     },
  //     error: (err)=>{
  //       //for fallback method in complete
  //     },
  //     complete: ()=>{
  //       this.order= dummyOrder;// This is fallback, since apis do not function now. TAKE OUT IN PROD VERSION
  //       console.log('done', this.order)
  //     }
  //   });
  // }

  updateOrder(order: orderTO) {
    this.order= {...order};
    console.log('updates order received at parent', this.order)
  }

  onSelect(selectedWorkflow: number | null) {
    this.selectedWorkflowIndex= selectedWorkflow;
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
              longDescription: 'New item: Description',
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
