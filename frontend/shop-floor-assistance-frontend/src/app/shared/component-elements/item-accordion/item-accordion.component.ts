import { ChangeDetectorRef, Component, EventEmitter, input, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { itemTO } from '../../../types/itemTO';
import { taskTO } from '../../../types/taskTO';
import { MatExpansionModule } from '@angular/material/expansion';
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
  // itemIndex!: number | null;
  itemIndices: {[workflowIndex: number]:{ [taskIndex: number]: number } } = {};

  constructor(private cdr:ChangeDetectorRef, private uiService: UIService){
    
  }

  ngOnInit(): void {
    this.itemIndices = this.uiService.getItemIndices();
    if (Object.keys(this.itemIndices).length === 0) {
      this.initializeItemIndices();
    }
    console.log('item indices after initialization',this.itemIndices);
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
          this.itemIndices[workflowIndex] = this.itemIndices[workflowIndex] || {};
          this.itemIndices[workflowIndex][taskIndex] = 0;
      //??
        });
      });
    }
  }


  ngOnChanges(changes: SimpleChanges): void {
    if(changes['workflowIndex'] ){

    }
    if(changes['order'] || changes['workflowIndex'] || changes['taskIndex'] || changes['itemIndex']){
      if(this.taskIndex != null && this.taskIndex >=0){
        this.getItemsForSelectedTask();
        this.initializeItemUIStates(); 
      }
    }
    
    if(changes['workflowIndex']){
      console.log('on wflow index change, itemindices is:', this.itemIndices)
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




  deleteItems(index: number,event: MouseEvent) {
    if(this.taskIndex!=null && this.workflowIndex !=null){
      event.stopPropagation();
      const task= this.order.workflows[this.workflowIndex].tasks[this.taskIndex];
      task.items.splice(index, 1);

      // ------------------change this suitably to use itemIndices array
      // this.itemIndex= task.items.length>0?0:null;
      // ---------------
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
     // ------------------change this suitably to use itemIndices array
    // this.itemIndex = index;
     // ------------------change this suitably to use itemIndices array
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
      // this.itemIndices[this.workflowIndex][this.taskIndex]=index;
      this.itemIndices= this.uiService.getItemIndices();
     
    }
     console.log('item indices after select',this.itemIndices)
  }

  isAnyWorkflowInEditMode(): boolean {
    return Object.values(this.itemUIStates).some(state => state.editMode);
  }

}
