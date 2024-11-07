import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { dummyOrder } from '../../types/dummyData';
import { WorkflowAccordionComponent } from '../../shared/component-elements/workflow-accordion/workflow-accordion.component';
import { TaskTabComponent } from '../../shared/component-elements/task-tab/task-tab.component';

@Component({
  selector: 'app-operator-view-workflow',
  standalone: true,
  imports: [RouterModule,  
    WorkflowAccordionComponent,
    TaskTabComponent
  ],
  templateUrl: './operator-view-workflow.component.html',
  styleUrl: './operator-view-workflow.component.css'
})
export class OperatorViewWorkflowComponent {
  showEditOptions: boolean = false;

  //   constructor(private route: ActivatedRoute) {
  //   this.route.params.subscribe(params => {
  //     console.log(params['id']); // access the id
  //   });
  // }

  // //or
  
  // name: string= 'intial vlue';
  // @Input() set id(value: string){
  //   this.name= value;
  //   console.log(this.name);
  // }
  


  constructor(private backendCommunicationService:BackendCommunicationService,
    private route: ActivatedRoute,){
    this.route.params.subscribe(params => {
      const orderId= params['id'];
      this.backendCommunicationService.getOperatorOrder(orderId).pipe(
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
    this.order= {...order};
    console.log('updates order received at parent', this.order)
  }

  onSelect(selectedWorkflow: number | null) {
    this.selectedWorkflowIndex= selectedWorkflow;
  }


}
