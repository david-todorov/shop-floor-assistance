import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { orderTO } from '../../../types/orderTO';
import { MatIconModule } from '@angular/material/icon';
import { workflowStates } from '../workflowUI-state';
import { workflowTO } from '../../../types/workflowTO';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { EditWorkflowDialogComponent } from '../edit-workflow-dialog/edit-workflow-dialog.component';


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
  newWorkflowInProgress: boolean = false;

   // Define newWorkflow property here
  newWorkflow = {
    name: '',
    description: ''
  };

  constructor(private cdr: ChangeDetectorRef, private dialog: MatDialog) {}

  
  ngOnInit(): void {
    this.selectedWorkflowIndex= 0;
    
  }
  
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
      // this.expandedPanels= new Array(this.order.workflows.length).fill(false);
      // this.selectedWorkflowIndex= 0;
    }
  }

addNewWorkflow() {
    if (this.newWorkflowInProgress) {
      return; // Prevent adding another workflow while one is in progress
    }
    const newWorkflow = {
      workflowNumber: (this.order.workflows.length + 1).toString(),
      name: '',
      description: '',
      tasks: []
    };
    this.order.workflows.push(newWorkflow);
    this.newWorkflowInProgress = true;
    this.onOrderUpdate.emit(this.order);
  }


 selectWorkflow(index: number) {
    if (this.workFlowStates[index].editMode || this.newWorkflowInProgress) {
      return; // Do not trigger selectWorkflow if in edit mode or new workflow is in progress
    }
    this.selectedWorkflowIndex = index;
    this.onSelect.emit(this.selectedWorkflowIndex);
  }

  deleteWorkflow(index: number, event: MouseEvent) {
    if (this.selectedWorkflowIndex !== null) {
      event.stopPropagation();
      this.order.workflows.splice(index, 1);
      delete this.workFlowStates[index];
      this.selectedWorkflowIndex = this.order.workflows.length > 0 ? 0 : null;
      this.initializeWorkflowStates();
      this.expandedPanels = new Array(this.order.workflows.length).fill(false); // Ensure all panels are closed
      this.onSelect.emit(this.selectedWorkflowIndex);
      this.onOrderUpdate.emit(this.order);
    }
  }

    doneAddingWorkflow(index: number) {
    if (!this.workFlowStates[index].updatedTitle || this.workFlowStates[index].updatedTitle.trim() === '') {
      alert('Workflow name cannot be empty!');
      return;
    }
    this.newWorkflowInProgress = false;
    this.onOrderUpdate.emit(this.order);
  }

  saveOrder(index: number){
    if(this.workFlowStates[index].updatedTitle=='' || this.workFlowStates[index].updatedTitle==null){
      alert('The workflow name cannot be empty!');
      return;
    }
    this.order.workflows[index].name= this.workFlowStates[index].updatedTitle;
    this.order.workflows[index].description= this.workFlowStates[index].updatedDescription;
    
  };
  

  toggleEditMode(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.expandedPanels[index] = !this.expandedPanels[index]; // Expand the panel
    this.selectedWorkflowIndex = index;
    this.cdr.detectChanges();
    this.workFlowStates[index].editMode = !this.workFlowStates[index].editMode;
    const saveMode= !this.workFlowStates[index].editMode; //save mode is opposite of edit mode
    if(saveMode){
      this.saveOrder(index);
      this.initializeWorkflowStates();
      this.expandedPanels = new Array(this.order.workflows.length).fill(false); // Ensure all panels are closed
      this.onOrderUpdate.emit(this.order);
      this.onSelect.emit(this.selectedWorkflowIndex);

    }
  }

  isAnyWorkflowInEditMode(): boolean {
    return Object.values(this.workFlowStates).some(state => state.editMode);
  }

  trackByIndex(index: number, item: any): any {
  return index;
}

  editWorkflow(workflowIndex: number): void {
    const workflow = this.order.workflows[workflowIndex];

    const dialogRef = this.dialog.open(EditWorkflowDialogComponent, {
      width: '400px',
      data: {
        name: workflow.name,
        description: workflow.description
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Update the workflow with the new values
        this.order.workflows[workflowIndex].name = result.taskname;
        this.order.workflows[workflowIndex].description = result.description;
        this.onOrderUpdate.emit(this.order); // Notify parent of changes
      }
    });
  }


}




