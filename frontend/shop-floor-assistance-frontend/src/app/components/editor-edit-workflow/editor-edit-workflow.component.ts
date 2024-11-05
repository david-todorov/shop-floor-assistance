import { Component, Input, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { dummyOrder } from '../../types/dummyData';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from '../../shared/component-elements/task-tab/task-tab.component';

@Component({
  selector: 'app-editor-edit-workflow',
  standalone: true,
  imports: [
    WorkflowAccordionComponent,
  TaskTabComponent],
  templateUrl: './editor-edit-workflow.component.html',
  styleUrl: './editor-edit-workflow.component.css'
})
export class EditorEditWorkflowComponent {


  constructor(private backendCommunicationService:BackendCommunicationService,
    private route: ActivatedRoute,){
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
        },
        error: (err)=>{
          //for fallback method in complete
        },
        complete: ()=>{
          this.order= dummyOrder;// This is fallback, since apis do not function now. TAKE OUT IN PROD VERSION
          console.log('done', this.order)
        }
      });
    });
  }


  order!: orderTO;
  selectedWorkflowIndex!: number | null;
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
    this.order= order;
  }

  onSelect(selectedWorkflow: number | null) {
    this.selectedWorkflowIndex= selectedWorkflow;
  }

}
