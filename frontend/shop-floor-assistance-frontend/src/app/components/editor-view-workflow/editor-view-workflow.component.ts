import { Component, Input, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { orderTO } from '../../types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { dummyOrder } from '../../types/dummyData';

@Component({
  selector: 'app-editor-view-workflow',
  standalone: true,
  imports: [],
  templateUrl: './editor-view-workflow.component.html',
  styleUrl: './editor-view-workflow.component.css'
})
export class EditorViewWorkflowComponent {

  
  //   constructor(private route: ActivatedRoute) {
  //   this.route.params.subscribe(params => {
  //     console.log(params['id']); // access the id
  //   });
  // }

  constructor(private backendCommunicationService:BackendCommunicationService){}

  order!: orderTO;
  // orderName: string='';
  // orderNumber: string= '';
  // description: string= '';

  // ngOnChanges(changes: SimpleChanges): void {
  //   if (changes['order'] && changes['order'].currentValue) {
  //   this.orderName= this.order.name;
  //   this.orderNumber= this.order.orderNumber;
  //   this.description= this.order.shortDescription;
  //   }
  // }

  @Input() set id(orderId: string){
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
  }
}
