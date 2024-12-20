import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { orderTO } from '../../types/orderTO';
import { MatIconModule } from '@angular/material/icon';
import { workflowStates } from '../../types/workflowUI-state';
import { workflowTO } from '../../types/workflowTO';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { taskTO } from '../../types/taskTO';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { MatDialog } from '@angular/material/dialog';
import { EditWorkflowDialogComponent } from '../edit-workflow-dialog/edit-workflow-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-workflow-accordion',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    DragDropModule
  ],
  templateUrl: './workflow-accordion.component.html',
  styleUrl: './workflow-accordion.component.css'
})
 /**
   * Workflow component
   * 
   * This file implements the workflow accordion which is used to display workflows. It is highly interactive,
   * supporting reordering and draging of workflows to and from the component. The workflow element
   * also supports editing and deletion of workflows from the parent workflow.
   * @author Jossin Antony
*/
export class WorkflowAccordionComponent implements OnInit, OnChanges{
   /**
   * Declaration of variables and input & outputs.
*/
  @Input() order!: orderTO;
  @Input() isEditorMode!: boolean;

  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onSelect = new EventEmitter<number | null>();

  orderExists: boolean= false;
  selectedWorkflowIndex: number | null = 0;
  workFlowStates: workflowStates= {};
  expandedPanels: boolean[] = [];

  constructor(private cdr: ChangeDetectorRef, 
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.selectedWorkflowIndex= 0;
    this.initializeWorkflowStates();
  }
  
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['order']) {
      this.initializeWorkflowStates();
    }
  }

   /**
   *Initialization of workflow states
  */
  initializeWorkflowStates() {
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

  /**
   *Emit the selected workflow so that the corresponding task element is displayed.
  */
  selectWorkflow(index: number) {
    if (this.workFlowStates[index].editMode) {
      return; // Do not trigger selectWorkflow if in edit mode
    }
    this.selectedWorkflowIndex = index;
    this.onSelect.emit(this.selectedWorkflowIndex);
  }

  /**
   *Handle deletion of workflows
  */
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

  /**
   *Save the order with the new workflow.
  */
  saveOrder(index: number){
    if(this.workFlowStates[index].updatedTitle=='' || this.workFlowStates[index].updatedTitle==null){
      alert('The workflow name cannot be empty!');
      return;
    }
    this.order.workflows[index].name= this.workFlowStates[index].updatedTitle;
    this.order.workflows[index].description= this.workFlowStates[index].updatedDescription;
  };
  

  /**
   *Handle UI behaviour according to read/edit mode of the workflow
  */
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

  /**
   *Editing of workflow
  */
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

  //handle drop event of a new workflow from the suggestions component
  drop(event: CdkDragDrop<workflowTO[]>): void {
    // moveItemInArray(this.order.workflows, event.previousIndex, event.currentIndex);
    if (event.previousContainer === event.container) {
      moveItemInArray(this.order.workflows, event.previousIndex, event.currentIndex);
    }else{
        transferArrayItem(
          event.previousContainer.data,
          this.order.workflows,
          event.previousIndex,
          event.currentIndex
        );
        this.showSnackbar('New item added to workflows!');
      }
    this.onOrderUpdate.emit(this.order);
  }

  /**
   *Show snackbar to indicate the additon of a new element to the user.
  */
  showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close',{
      duration: 1500
    });
  }
}
