import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { orderTO } from '../../../types/orderTO';
import { MatIconModule } from '@angular/material/icon';
import { workflowTO } from '../../../types/workflowTO';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { taskTO } from '../../../types/taskTO';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';
import { MatDialog } from '@angular/material/dialog';


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
    DragDropModule
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
  expandedPanels: boolean[] = [];
 

  constructor(private cdr: ChangeDetectorRef, private dialog: MatDialog) {}
  ngOnChanges(changes: SimpleChanges): void {
    throw new Error('Method not implemented.');
  }

  
  ngOnInit(): void {
    this.selectedWorkflowIndex= 0;
    
  }
  
  ngAfterViewInit(): void {
    // this.cdr.detectChanges();
  }
  


  selectWorkflow(index: number) {
    this.selectedWorkflowIndex = index;
    this.onSelect.emit(this.selectedWorkflowIndex);
  }

  


  trackByIndex(index: number, item: any): any {
  return index;
}


}




