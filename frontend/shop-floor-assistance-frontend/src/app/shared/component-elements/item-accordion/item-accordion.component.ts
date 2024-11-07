import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { itemTO } from '../../../types/itemTO';
import { taskTO } from '../../../types/taskTO';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { itemFlowStates } from '../workflowUI-state';

@Component({
  selector: 'app-item-accordion',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './item-accordion.component.html',
  styleUrl: './item-accordion.component.css'
})
export class ItemAccordionComponent implements OnChanges{



selectItemflow(index: number) {
  if (this.itemFlowStates[index].editMode) {
    return; // Do not trigger selectWorkflow if in edit mode
  }
  this.selectedItemIndex = index;
}

isAnyWorkflowInEditMode(): boolean {
  return Object.values(this.itemFlowStates).some(state => state.editMode);
}


  @Input() selectedTasks!: taskTO[];
  @Input() selectedTab!: number | null;

  @Output() updatedItemsFromTasks = new EventEmitter<itemTO[]>();
 
  constructor(private cdr:ChangeDetectorRef){}


  expandedPanels: boolean[] = [];
  doneAll: boolean= true;

  items!:itemTO[];
  itemFlowStates: itemFlowStates= {};
  selectedItemIndex!: number | null;
  itemsExist!: boolean;

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['selectedTasks'] || changes['selectedTab']){
      if(this.selectedTab != null && this.selectedTab >=0){
        this.getItemsForSelectedTask();
        this.initializeItemflowStates(); 
      }
    }
  }


   initializeItemflowStates() {
      this.items.forEach((item: itemTO, index: number) => {
        this.itemFlowStates[index] = { editMode: false, 
          showDescription: false, 
          updatedTitle: item.name,
          updatedDescription: item.longDescription,
          upDatedTimeReq: item.timeRequired,
          checkStatus: false};
      });
      this.expandedPanels= new Array(this.items.length).fill(false);
    }




  onAccordionClick(index: number): void {
    console.log('Accordion element clicked:', index);
    this.selectedItemIndex= index;
  }

   getItemsForSelectedTask() {
    if(this.selectedTab !== null && 
      this.selectedTab !== undefined) {
      this.items = [...this.selectedTasks[this.selectedTab].items]; // Create a new array reference
    }else{
      this.items=[];
    }
  }


  deleteItems(index: number,event: MouseEvent) {
    if(this.selectedTab!=null){
      event.stopPropagation();
      this.selectedTasks[this.selectedTab].items.splice(index, 1);
      console.log('index before', this.selectedItemIndex)

      // if(index<0) this.selectedItemIndex= -1;
      // else if(index>0 && index< this.selectedTasks[this.selectedTab].items.length) this.selectedItemIndex= index-1;
      
      if(this.selectedTasks[this.selectedTab].items.length>0){
        this.selectedItemIndex=0;
      }else{
        this.selectedItemIndex=null;
      }

      this.items = [...this.selectedTasks[this.selectedTab].items]; // Create a new array reference
      this.initializeItemflowStates(); 
      this.updatedItemsFromTasks.emit(this.items);
      console.log('in delete items', this.selectedTasks[this.selectedTab].items)
    }
  }

  saveItems(index: number){
    if(this.itemFlowStates[index].updatedTitle=='' || this.itemFlowStates[index].updatedTitle==null){
      alert('The item name cannot be empty!');
      return;
    }
    this.items[index].name= this.itemFlowStates[index].updatedTitle;
    this.items[index].longDescription= this.itemFlowStates[index].updatedDescription;
    this.items[index].timeRequired= this.itemFlowStates[index].upDatedTimeReq;
  };

  toggleEditMode(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.expandedPanels[index] = !this.expandedPanels[index]; // Expand the panel
    this.selectedItemIndex = index;
    this.cdr.detectChanges();
    this.itemFlowStates[index].editMode = !this.itemFlowStates[index].editMode;
    const saveMode= !this.itemFlowStates[index].editMode; //save mode is opposite of edit mode
    if(saveMode){
      this.saveItems(index);
      this.initializeItemflowStates();
      this.updatedItemsFromTasks.emit(this.items);
    }
  }


}
