import { Component, Input, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { dummyOrder } from '../../types/dummyData';
import { EditorAccordionComponent } from '../../shared/component-elements/editor-accordion/editor-accordion.component';

@Component({
  selector: 'app-editor-view-workflow',
  standalone: true,
  imports: [EditorAccordionComponent],
  templateUrl: './editor-view-workflow.component.html',
  styleUrl: './editor-view-workflow.component.css'
})
export class EditorViewWorkflowComponent {

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
    console.log('updated order')
  }
}
