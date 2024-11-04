import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { orderTO } from '../../../types/orderTO';
import { workflowTO } from '../../../types/workflowTO';
import { MatIconModule } from '@angular/material/icon';
import { workflowStates } from '../workflowUI-state';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-editor-accordion',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './editor-accordion.component.html',
  styleUrl: './editor-accordion.component.css'
})
export class EditorAccordionComponent implements OnInit, OnChanges, AfterViewInit{

  @Input() order!: orderTO;
  @Input() doneAll: boolean= true;
  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onSelect = new EventEmitter<number>();
  orderExists: boolean= false;
  selectedWorkflowIndex: number= 0;
  workFlowStates: workflowStates= {};
  expandedPanels: boolean[] = [];

  

  constructor(private cdr:ChangeDetectorRef){}
  
  ngOnInit(): void {}
  
  ngAfterViewInit(): void {
    // this.cdr.detectChanges();
  }
  
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['order']) {
      this.initializeWorkflowStates();
    }
  }

  initializeWorkflowStates() {
    this.orderExists= (this.order!== null && this.order != undefined);
    if(this.orderExists){
      this.order.workflows.forEach((workflow: any, index: number) => {
        this.workFlowStates[index] = { editMode: false, 
          showDescription: false, 
          updatedTitle: workflow.name,
          updatedDescription: workflow.description};
      });
      this.expandedPanels= new Array(this.order.workflows.length).fill(false);
    }
  }

  selectWorkflow(index: number) {
    this.selectedWorkflowIndex = index;
    this.onSelect.emit(this.selectedWorkflowIndex);
  }



  deleteWorkflow(index: number) {
    // Handle delete action
    console.log('Deleting at index:', index);
     this.onOrderUpdate.emit(this.order);
  }

  saveOrder(index: number){
    if(this.workFlowStates[index].updatedTitle=='' || this.workFlowStates[index].updatedTitle==null){
      alert('The workflow name cannot be empty!');
      return;
    }
    this.order.workflows[index].name= this.workFlowStates[index].updatedTitle;
    this.onOrderUpdate.emit(this.order);
    this.onSelect.emit(this.selectedWorkflowIndex);
  };
  

  toggleEditMode(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.expandedPanels[index] = !this.expandedPanels[index]; // Expand the panel
    this.selectedWorkflowIndex = index;
    this.cdr.detectChanges();
    this.workFlowStates[index].editMode = !this.workFlowStates[index].editMode;
    const saveMode= !this.workFlowStates[index].editMode; //sava emode is opposite of edit mode
    if(saveMode){
      this.saveOrder(index);
      this.order.workflows.forEach((workflow)=>{
        console.log(workflow.name);
      })
    }
   
  }
}
