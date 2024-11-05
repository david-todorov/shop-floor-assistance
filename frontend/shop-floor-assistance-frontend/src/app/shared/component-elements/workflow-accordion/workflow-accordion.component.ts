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
  selector: 'app-workflow-accordion',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './workflow-accordion.component.html',
  styleUrl: './workflow-accordion.component.css'
})
export class WorkflowAccordionComponent implements OnInit, OnChanges, AfterViewInit{

  @Input() order!: orderTO;
  @Input() doneAll: boolean= true;
  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onSelect = new EventEmitter<number | null>();
  orderExists: boolean= false;
  selectedWorkflowIndex: number | null = 0;
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
      this.selectedWorkflowIndex= 0;
    }
  }

  selectWorkflow(index: number) {
    this.selectedWorkflowIndex = index;
    //emit order and selected workflow index
    this.onSelect.emit(this.selectedWorkflowIndex);
    this.onOrderUpdate.emit(this.order);
    console.log('in select workflow')
  }

   deleteWorkflow(index: number, event: MouseEvent) {
    if (this.selectedWorkflowIndex !== null) {
      event.stopPropagation();
      this.order.workflows.splice(index, 1);
      delete this.workFlowStates[index];
      if (this.selectedWorkflowIndex === index) {//no elements left case
        this.selectedWorkflowIndex = null;
      } else if (this.selectedWorkflowIndex > index) {
        this.selectedWorkflowIndex--;
      }
      // this.onSelect.emit(this.selectedWorkflowIndex);
      // this.onOrderUpdate.emit(this.order);
      this.initializeWorkflowStates();
    }else{
      //this.onSelect.emit(this.selectedWorkflowIndex);
      //this.onOrderUpdate.emit(this.order);
    }
    this.onSelect.emit(this.selectedWorkflowIndex);
    this.onOrderUpdate.emit(this.order);
      console.log('in delete')
      console.log('in delete', this.order, this.selectedWorkflowIndex)
  }

  saveOrder(index: number){
    if(this.workFlowStates[index].updatedTitle=='' || this.workFlowStates[index].updatedTitle==null){
      alert('The workflow name cannot be empty!');
      return;
    }
    this.order.workflows[index].name= this.workFlowStates[index].updatedTitle;
    this.order.workflows[index].description= this.workFlowStates[index].updatedDescription;
    // this.onOrderUpdate.emit(this.order);
    this.onSelect.emit(this.selectedWorkflowIndex);
    this.onOrderUpdate.emit(this.order);
    // if(this.selectedWorkflowIndex != null){
      // this.onSelect.emit(this.selectedWorkflowIndex);
    // }
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
        console.log(workflow.name, workflow.description);
      })
    }
  }
}




