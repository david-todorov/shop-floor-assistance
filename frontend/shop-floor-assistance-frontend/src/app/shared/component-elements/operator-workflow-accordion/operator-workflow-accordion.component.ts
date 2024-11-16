import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { orderTO } from '../../../types/orderTO';
import { MatIconModule } from '@angular/material/icon';
import { workflowStates } from '../workflowUI-state';
import { workflowTO } from '../../../types/workflowTO';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { taskTO } from '../../../types/taskTO';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';
import { OperatorTaskTabComponent } from '../operator-task-tab/operator-task-tab.component';



@Component({
  selector: 'app-operator-workflow-accordion',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    DragDropModule,
    OperatorTaskTabComponent
  ],
  templateUrl: './operator-workflow-accordion.component.html',
  styleUrl: './operator-workflow-accordion.component.css'
})


export class OperatorWorkflowAccordionComponent implements OnInit, OnChanges, AfterViewInit{

 drop(event: CdkDragDrop<workflowTO[]>): void {
    moveItemInArray(this.order.workflows, event.previousIndex, event.currentIndex);
    this.onOrderUpdate.emit(this.order);
  }

  @Input() order!: orderTO;

  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onSelect = new EventEmitter<number | null>();

  orderExists: boolean= false;
  selectedWorkflowIndex: number | null = 0;
  workFlowStates: workflowStates= {};
  expandedPanels: boolean[] = [];

 constructor(private cdr: ChangeDetectorRef) {}

  
  ngOnInit(): void {
    this.selectedWorkflowIndex= 0;
    this.initializeWorkflowStates();
    
  }

ngOnChanges(changes: SimpleChanges): void {
    if (changes['order']) {
      this.initializeWorkflowStates();
    }
  }


  initializeWorkflowStates() {
    console.log(this.order)
    this.orderExists= (this.order!== null && this.order != undefined);
    if(this.orderExists){
      this.order.workflows.forEach((workflow: any, index: number) => {
        this.workFlowStates[index] = { editMode: false, 
          showDescription: false, 
          updatedTitle: workflow.name,
          updatedDescription: workflow.description};
      });
    }
  }
  
  ngAfterViewInit(): void {
    // this.cdr.detectChanges();
  }
  


 selectWorkflow(index: number) {
    if (this.workFlowStates[index].editMode) {
      return; // Do not trigger selectWorkflow if in edit mode
    }
    this.selectedWorkflowIndex = index;
    this.onSelect.emit(this.selectedWorkflowIndex);
  }



  trackByIndex(index: number, item: any): any {
  return index;
}


}




