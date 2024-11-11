import { ChangeDetectorRef, Component, EventEmitter, input, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { itemTO } from '../../../types/itemTO';
import { taskTO } from '../../../types/taskTO';
import { MatAccordion, MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { itemUIStates, workflowCheckedStatus } from '../workflowUI-state';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { orderTO } from '../../../types/orderTO';
import { UIService } from '../../../services/ui.service';


@Component({
  selector: 'app-item-accordion',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCheckboxModule
  ],
  templateUrl: './item-accordion.component.html',
  styleUrl: './item-accordion.component.css'
})
export class ItemAccordionComponent implements OnInit, OnChanges, OnDestroy{

  @Input() order!:orderTO;
  @Input() workflowIndex!:number | null;
  @Input() taskIndex!: number | null;
  
  @Output() onOrderUpdate= new EventEmitter<orderTO>();

  expandedPanels: boolean[] = [];
  items!:itemTO[];
  itemUIStates: itemUIStates= {};

  itemIndices: {[workflowIndex: number]:{ [taskIndex: number]: number } } = {};
  checkStatuses: {[workflow: number]: { [task: number]: {[item: number]: boolean}}} = {};

  @ViewChild(MatAccordion) accordion!: MatAccordion;

  constructor(private cdr:ChangeDetectorRef, private uiService: UIService){}

  ngOnInit(): void {
    this.itemIndices = this.uiService.getItemIndices();
    if (Object.keys(this.itemIndices).length === 0) {
      this.initializeItemIndices();
      
    }
    this.checkStatuses = this.uiService.getItemStatuses();
    if (Object.keys(this.checkStatuses).length === 0) {
      this.initializeCheckStatuses();
      
    }
    // console.log('item indices after initialization',this.itemIndices);
    console.log('check statuses after initialization',this.checkStatuses);
  }

  ngOnDestroy(): void {
    this.uiService.setItemIndices(this.itemIndices);
  }

  initializeItemIndices(): void {
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        workflow.tasks.forEach((task, taskIndex) => {
          this.uiService.setSelectedItemIndex(workflowIndex, taskIndex, 0);
          // ???
          // this.itemIndices[workflowIndex] = this.itemIndices[workflowIndex] || {};
          // this.itemIndices[workflowIndex][taskIndex] = 0;
        });
      });
    }
  }

    initializeCheckStatuses(): void {
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        workflow.tasks.forEach((task, taskIndex) => {
          task.items.forEach((item, itemIndex) => {
            this.checkStatuses[workflowIndex] = this.checkStatuses[workflowIndex] || {};
            this.checkStatuses[workflowIndex][taskIndex] = this.checkStatuses[workflowIndex][taskIndex] || {};
            this.checkStatuses[workflowIndex][taskIndex][itemIndex] = false;
            this.uiService.setItemStatuses(this.checkStatuses);
          });
        });
      });
    }
  }


  ngOnChanges(changes: SimpleChanges): void {
    if(changes['workflowIndex'] ){
      this.closeAllPanels();
      console.log('on wflow index change, itemindices is:', this.itemIndices)
    }
    if(changes['order'] || changes['workflowIndex'] || changes['taskIndex'] || changes['itemIndex']){
      if(this.taskIndex != null && this.taskIndex >=0){
        this.getItemsForSelectedTask();
        this.initializeItemUIStates(); 
        this.cleanUpCheckStatuses();
      }
    }
  }

  initializeItemUIStates() {//keep the itemUIStates in tandem with item update/delete/ etc.
    this.items.forEach((item: itemTO, index: number) => {
      this.itemUIStates[index] = { editMode: false, 
        showDescription: false, 
        updatedTitle: item.name,
        updatedDescription: item.longDescription,
        upDatedTimeReq: item.timeRequired};
    });
  }

  getItemsForSelectedTask() {
    if(this.taskIndex != null && this.workflowIndex !=null){
      const task= this.order.workflows[this.workflowIndex].tasks[this.taskIndex]
      // this.itemIndices= this.selectedItemService.getItemIndices();
      this.items = [...task.items]; // Create a new array reference
    }else{
      this.items=[];
    }
  }

  cleanUpCheckStatuses(): void {
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

  closeAllPanels(): void {
    if (this.accordion) {
      this.accordion.closeAll();
    }
  }


  deleteItems(index: number,event: MouseEvent) {
    if(this.taskIndex!=null && this.workflowIndex !=null){
      event.stopPropagation();
      const task= this.order.workflows[this.workflowIndex].tasks[this.taskIndex];
      task.items.splice(index, 1);
            
      // Update checkStatuses
      delete this.checkStatuses[this.workflowIndex][this.taskIndex][index];

      // this.itemIndex= task.items.length>0?0:null;
      this.items = [...task.items]; // Create a new array reference
      this.initializeItemUIStates(); 
      this.expandedPanels= new Array(this.items.length).fill(false);
      this.onOrderUpdate.emit(this.order);
    }
  }

  saveItems(index: number){
    if(this.itemUIStates[index].updatedTitle=='' || this.itemUIStates[index].updatedTitle==null){
      alert('The item name cannot be empty!');
      return;
    }
    this.items[index].name= this.itemUIStates[index].updatedTitle;
    this.items[index].longDescription= this.itemUIStates[index].updatedDescription;
    this.items[index].timeRequired= this.itemUIStates[index].upDatedTimeReq;
  }

  toggleEditMode(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.expandedPanels[index] = !this.expandedPanels[index]; // Expand the panel
    // this.itemIndex = index;
    this.cdr.detectChanges();
    this.itemUIStates[index].editMode = !this.itemUIStates[index].editMode;
    const saveMode= !this.itemUIStates[index].editMode; //save mode is opposite of edit mode
    if(saveMode){
      this.saveItems(index);
      this.initializeItemUIStates();
      this.expandedPanels= new Array(this.items.length).fill(false);
      this.onOrderUpdate.emit(this.order);
    }
  }

  selectItem(index: number) {
    console.log('w', this.workflowIndex, 't', this.taskIndex, 'i', this.itemIndices)
    if (this.itemUIStates[index].editMode) {
      return; // Do not trigger selectWorkflow if in edit mode
    }
    if(this.workflowIndex!=null && this.taskIndex!=null){
      this.uiService.setSelectedItemIndex(this.workflowIndex, this.taskIndex, index);
      this.itemIndices= this.uiService.getItemIndices();
    }
     console.log('check statuses after select',this.checkStatuses)
      console.log('check indices after select',this.itemIndices)
  }

  isAnyWorkflowInEditMode(): boolean {
    return Object.values(this.itemUIStates).some(state => state.editMode);
  }

  updateCheckStatus(workflowIndex: number, taskIndex: number, itemIndex: number, checked: boolean): void {
    this.checkStatuses[workflowIndex][taskIndex][itemIndex] = checked;
    this.uiService.setSelectedItemStatus(workflowIndex, taskIndex, itemIndex, checked);
  }

}
