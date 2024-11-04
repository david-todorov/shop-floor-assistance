import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { orderTO } from '../../../types/orderTO';
import { workflowTO } from '../../../types/workflowTO';

interface Item {
  title: string;
  content: string;
}


@Component({
  selector: 'app-editor-accordion',
  standalone: true,
  imports: [ MatExpansionModule,
    MatButtonModule,
  CommonModule],
  templateUrl: './editor-accordion.component.html',
  styleUrl: './editor-accordion.component.css'
})
export class EditorAccordionComponent {
  
  @Input() order!: orderTO;
  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onSelect = new EventEmitter<number>();

  selectedWorkflowIndex: number | null = null;

  
  selectWorkflow(index: number) {
    this.selectedWorkflowIndex = index;
    this.onSelect.emit(this.selectedWorkflowIndex);
    console.log('emitted', this.selectedWorkflowIndex)
  }

  editWorkflow(workflow: workflowTO) {
    // Handle edit action
    console.log('Editing', workflow);
     this.onOrderUpdate.emit(this.order);
  }

  deleteWorkflow(workflow: workflowTO) {
    // Handle delete action
    console.log('Deleting', workflow);
     this.onOrderUpdate.emit(this.order);
  }
}
