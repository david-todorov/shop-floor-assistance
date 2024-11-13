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
  selector: 'app-operator-workflow-accordion',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './operator-workflow-accordion.component.html',
  styleUrls: ['./operator-workflow-accordion.component.css']
})



export class OperatorWorkflowAccordionComponent implements OnInit, OnChanges, AfterViewInit{

  @Input() order!: orderTO;
  expandedPanels: boolean[] = [];
  selectedWorkflow: workflowTO | null = null;

  constructor(private changeDetectorRef: ChangeDetectorRef) {}

    ngOnInit(): void {
    this.expandedPanels = new Array(this.order.workflows.length).fill(false);
    this.selectedWorkflow = this.order.workflows[0] || null; // Default to first workflow
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['order'] && changes['order'].currentValue) {
      this.initializeExpandedPanels();
    }
  }

  ngAfterViewInit(): void {
    this.changeDetectorRef.detectChanges();
  }

  // Initialize expanded panels based on the number of workflows
  private initializeExpandedPanels(): void {
    this.expandedPanels = new Array(this.order?.workflows?.length || 0).fill(false);
  }

  // Track by index to ensure efficient rendering
  trackByIndex(index: number): number {
    return index;
  }

 

  // Method to select a workflow and show its tasks
  selectWorkflow(index: number): void {
    this.expandedPanels = this.expandedPanels.map((_, i) => i === index);
    this.selectedWorkflow = this.order.workflows[index];
    console.log(`Selected workflow: ${this.selectedWorkflow.name}`);
  }

  // Handle checkbox change for workflow items
  onCheckboxChange(event: Event, workflowIndex: number, itemIndex: number): void {
    const checked = (event.target as HTMLInputElement).checked;
    console.log(`Checkbox for item ${itemIndex} in workflow ${workflowIndex} is now ${checked ? 'checked' : 'unchecked'}`);
    // Additional logic for handling the checkbox change can go here
  }
}
