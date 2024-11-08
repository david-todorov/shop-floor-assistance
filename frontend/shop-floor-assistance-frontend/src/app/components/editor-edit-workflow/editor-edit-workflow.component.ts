import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { dummyOrder } from '../../types/dummyData';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from '../../shared/component-elements/task-tab/task-tab.component';
import { workflowCheckedStatus } from '../../shared/component-elements/workflowUI-state';





@Component({
  selector: 'app-editor-edit-workflow',
  standalone: true,
  imports: [
    WorkflowAccordionComponent,
    TaskTabComponent],
  templateUrl: './editor-edit-workflow.component.html',
  styleUrl: './editor-edit-workflow.component.css'
})
export class EditorEditWorkflowComponent implements OnInit {

  order!: orderTO;
  selectedWorkflowIndex!: number | null;
  operatorCheckedStatus!: workflowCheckedStatus[];

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
          console.log('done', this.order);

        }
      });
    });
  }

  
  ngOnInit(): void {
     this.initializeOperatorCheckedStatus();
  }

  initializeOperatorCheckedStatus(): void {
    if (this.order && this.order.workflows) {
      this.operatorCheckedStatus = this.order.workflows.map(workflow => ({
        tasks: workflow.tasks.map(task => ({
          items: task.items.map(() => ({ checked: false }))
        }))
      }));
    }
  }

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

  // allWorkflowsComplete($event: boolean) {
  //   console.log('allworkflows complete')
  // }
  
  onTasksChecked($event: workflowCheckedStatus[]) {
    console.log('tasks checked received')
  }

}
