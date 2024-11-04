import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { orderTO } from '../../../types/orderTO';
import { workflowTO } from '../../../types/workflowTO';
import { MatIconModule } from '@angular/material/icon';
import { workflowStates } from '../workflowUI-state';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-editor-accordion',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    FormsModule
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
  selectedWorkflowIndex: number | null = null;
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
          updatedTitle:'',
          updatedDescription:''};
      });
      this.expandedPanels= new Array(this.order.workflows.length).fill(false);
    }
  }

  selectWorkflow(index: number) {
    this.selectedWorkflowIndex = index;
    this.onSelect.emit(this.selectedWorkflowIndex);
  }

  editWorkflow(workflow: workflowTO) {
    // Handle edit action
    console.log('Editing', workflow);
     this.onOrderUpdate.emit(this.order);
  }

  deleteWorkflow(index: number) {
    // Handle delete action
    console.log('Deleting at index:', index);
     this.onOrderUpdate.emit(this.order);
  }

  toggleDescription(arg0: any,$event: MouseEvent) {
    throw new Error('Method not implemented.');
  }
  

  toggleEditMode(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.expandedPanels[index] = !this.expandedPanels[index]; // Expand the panel
    this.selectedWorkflowIndex = index;
    this.cdr.detectChanges();
    this.workFlowStates[index].editMode = !this.workFlowStates[index].editMode;
    let editOn= this.workFlowStates[index].editMode;
    if(!editOn){
        this.order.workflows[index].name= this.workFlowStates[index].updatedTitle;
    }
    this.order.workflows.forEach((workflow: workflowTO)=>{
      console.log(workflow.name, workflow.description)
    });
    //this.saveOrder();
  }
}
