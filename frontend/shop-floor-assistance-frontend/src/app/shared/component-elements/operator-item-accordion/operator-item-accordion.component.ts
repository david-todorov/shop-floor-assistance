import { ChangeDetectorRef, Component, EventEmitter, input, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { itemTO } from '../../../types/itemTO';
import { taskTO } from '../../../types/taskTO';
import { MatAccordion, MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { itemUIStates } from '../workflowUI-state';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { orderTO } from '../../../types/orderTO';
import { UIService } from '../../../services/ui.service';
import { ButtonComponent } from '../button/button.component';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';



@Component({
  selector: 'app-operator-item-accordion',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    ButtonComponent,
    DragDropModule
  ],
  templateUrl: './operator-item-accordion.component.html',
  styleUrl: './operator-item-accordion.component.css'
})
export class OperatoritemAccordionComponent implements OnInit, OnChanges, OnDestroy{

    @Input() order!: orderTO;
  @Input() workflowIndex!: number | null;
  @Input() taskIndex!: number | null;
  @Output() onOrderUpdate = new EventEmitter<orderTO>();

  expandedPanels: boolean[] = [];
  items: itemTO[] = [];

    //-Helper variables to maintain UI state
  itemUIStates: itemUIStates= {};
  itemIndices: {[workflowIndex: number]:{ [taskIndex: number]: number } } = {};
  checkStatuses: {[workflow: number]: { [task: number]: {[item: number]: boolean}}} = {};

//-------------------------------------------------------------

  @ViewChild(MatAccordion) accordion!: MatAccordion;

  constructor(private cdr: ChangeDetectorRef, private uiService: UIService) {}

drop(event: CdkDragDrop<itemTO[]>): void {
      if(this.workflowIndex!=null && this.taskIndex!=null){
        const items= this.order.workflows[this.workflowIndex].tasks[this.taskIndex].items
        moveItemInArray(items, event.previousIndex, event.currentIndex);
      }
    this.onOrderUpdate.emit(this.order);
  }
  //-----------------------------------------

  //--life cycle hooks
  ngOnInit(): void {
   this.checkStatuses = this.uiService.getItemStatuses();
    if (Object.keys(this.checkStatuses).length === 0) {
      this.initializeCheckStatuses();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['workflowIndex'] ){
      this.closeAllPanels();
    }
    if(changes['order'] || changes['workflowIndex'] || changes['taskIndex'] || changes['itemIndex']){
      if(this.taskIndex != null && this.taskIndex >=0){
        this.getItemsForSelectedTask();

        this.cleanUpCheckStatuses();
      }
    }
  }

    ngOnDestroy(): void {
    this.uiService.setItemStatuses(this.checkStatuses);
  }
  //---------------------------------------------------------------
  //--Ui helper functions for maintaing itemindices and checkstatuses

  initializeCheckStatuses(): void {
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        workflow.tasks.forEach((task, taskIndex) => {
          task.items.forEach((item, itemIndex) => {
            this.checkStatuses[workflowIndex] = this.checkStatuses[workflowIndex] || {};
            this.checkStatuses[workflowIndex][taskIndex] = this.checkStatuses[workflowIndex][taskIndex] || {};
            this.checkStatuses[workflowIndex][taskIndex][itemIndex] = false;
          });
        });
      });
    }
  }

  cleanUpCheckStatuses(): void {
     this.checkStatuses = this.uiService.getItemStatuses();
    const newCheckStatuses: { [workflowIndex: number]: { [taskIndex: number]: { [itemIndex: number]: boolean } } } = {};
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        newCheckStatuses[workflowIndex] = {};
        workflow.tasks.forEach((task, taskIndex) => {
          newCheckStatuses[workflowIndex][taskIndex] = {};
          task.items.forEach((item, itemIndex) => {
            if (this.checkStatuses[workflowIndex] && this.checkStatuses[workflowIndex][taskIndex] && this.checkStatuses[workflowIndex][taskIndex][itemIndex] !== undefined) {
              newCheckStatuses[workflowIndex][taskIndex][itemIndex] = this.checkStatuses[workflowIndex][taskIndex][itemIndex];
            } else {
              newCheckStatuses[workflowIndex][taskIndex][itemIndex] = false;
            }
          });
        });
      });
    }
    this.checkStatuses = newCheckStatuses;
    this.uiService.setItemStatuses(this.checkStatuses);
  }

  //----------------------------------------

  getItemsForSelectedTask() {
    if(this.taskIndex != null && this.workflowIndex !=null){
      const task= this.order.workflows[this.workflowIndex].tasks[this.taskIndex]
      this.items = [...task.items]; // Create a new array reference
    }else{
      this.items=[];
    }
  }


  updateCheckStatus(workflowIndex: number, taskIndex: number, itemIndex: number, checked: boolean): void {
    this.checkStatuses[workflowIndex][taskIndex][itemIndex] = checked;
    this.uiService.setSelectedItemStatus(workflowIndex, taskIndex, itemIndex, checked);
    this.onOrderUpdate.emit(this.order);
  }
  
  closeAllPanels(): void {
    if (this.accordion) {
      this.accordion.closeAll();
    }
  }

}
