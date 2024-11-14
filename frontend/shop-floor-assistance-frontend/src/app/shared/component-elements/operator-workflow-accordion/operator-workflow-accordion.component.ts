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
import { OperatorTaskTabComponent } from '../operator-task-tab/operator-task-tab.component';
import { itemTO } from '../../../types/itemTO';



@Component({
  selector: 'app-operator-workflow-accordion',
  standalone: true,
  imports: [ 
    OperatorTaskTabComponent,
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
  orderExists: boolean= false;

  constructor(private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.initializeExpandedPanels();
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

  // Method to select a workflow and show its tasks
  selectWorkflow(index: number): void {
    this.expandedPanels = this.expandedPanels.map((_, i) => i === index);
    this.selectedWorkflow = this.order.workflows[index];
    console.log(`Selected workflow: ${this.selectedWorkflow.name}`);
  }

  isAnyWorkflowInEditMode(): boolean {
    return false; // Placeholder logic if edit mode management is not implemented
  }

    trackByIndex(index: number, item: any): any {
  return index;
}
}
