import { ChangeDetectorRef, Component, EventEmitter, input, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
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
export class ItemAccordionComponent implements OnInit, OnChanges{

  @Input() order!:orderTO;
  @Input() workflowIndex!:number | null;
  @Input() taskIndex!: number | null;
  
  @Output() onOrderUpdate= new EventEmitter<orderTO>();

  expandedPanels: boolean[] = [];
  items!:itemTO[];
  itemUIStates: itemUIStates= {};
  itemIndex!: number | null;

  constructor(private cdr:ChangeDetectorRef){}

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges): void {
    console.log('wflowindex:',this.workflowIndex, 'taskIndex',this.taskIndex)
    if(changes['order'] || changes['workflowIndex'] || changes['taskIndex']){
      if(this.taskIndex != null && this.taskIndex >=0){
        this.getItemsForSelectedTask();
        this.initializeItemUIStates(); 
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

      this.itemIndex= task.items.length>0?0:null;
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
    this.itemIndex = index;
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

  selectItemflow(index: number) {
  if (this.itemUIStates[index].editMode) {
    return; // Do not trigger selectWorkflow if in edit mode
  }
  this.itemIndex = index;
  }

  isAnyWorkflowInEditMode(): boolean {
    return Object.values(this.itemUIStates).some(state => state.editMode);
  }

}
