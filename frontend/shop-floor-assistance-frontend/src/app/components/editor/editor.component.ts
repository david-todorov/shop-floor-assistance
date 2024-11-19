import { Component, OnInit } from '@angular/core';
import { OrderTableComponent } from '../../shared/component-elements/order-table/order-table.component';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { Router, RouterLink } from '@angular/router';
import { orderTO } from '../../shared/types/orderTO';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { sampleOrders } from '../../shared/types/dummyData';

@Component({
  selector: 'app-editor',
  standalone: true,
    imports: [OrderTableComponent,
    ButtonComponent,
  ],
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css'
})
export class EditorComponent implements OnInit{
  btnLabel: string= 'Start Wizard';
  order!: orderTO;
  loadedOrders!: orderTO[];
  viewDisabled: boolean= false;
  editDisabled: boolean= false;
  createDisabled: boolean= true;
  viewBtnLabel: string= 'View Workflow';
  editBtnLabel: string= 'Edit Workflow';
  createBtnLabel: string= 'Create Workflow';

  constructor(private router:Router,
    private backendCommunicationService: BackendCommunicationService
  ){}

  ngOnInit(): void {
    this.backendCommunicationService.getAllOrders().pipe(
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

        this.loadedOrders= sampleOrders;// This is fallback, since apis do not function now. TAKE OUT IN PROD VERSION
        console.log('done', this.loadedOrders)
      }
  });

  }
  
  orderSelected($event: any) {
    this.order= $event
  }
  resolveButtonClick($event: any) {
    if($event.type==='click'){
      if(this.order === null || this.order === undefined){
        alert('You must specify an order!');
      }else{
      console.log(this.order);
      this.router.navigate(['/editor/', this.order.orderNumber ]);
     
      }
    }
     return;
  }
}
