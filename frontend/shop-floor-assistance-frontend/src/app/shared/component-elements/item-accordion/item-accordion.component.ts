import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { itemTO } from '../../../types/itemTO';
import { taskTO } from '../../../types/taskTO';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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


toggleEditMode(_t4: number,$event: MouseEvent) {
// throw new Error('Method not implemented.');
}
selectWorkflow(_t4: number) {
// throw new Error('Method not implemented.');
}
isAnyWorkflowInEditMode(): void {
// throw new Error('Method not implemented.');
}


  @Input() selectedTasks!: taskTO[];
  @Input() selectedTab!: number | null;

  @Output() updatedItemsFromTasks = new EventEmitter<itemTO[]>();
 


  expandedPanels: boolean[] = [];
  doneAll: boolean= true;

  items!:itemTO[];
  selectedItemIndex!: number | null;

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['selectedTasks'] || changes['selectedTab']){
      if(this.selectedTab != null && this.selectedTab >=0){
        this.getItemsForSelectedTask();
        console.log(' received in items', this.selectedTab)
      }
    }
  }


  onAccordionClick(index: number): void {
    console.log('Accordion element clicked:', index);
    this.selectedItemIndex= index;
  }

   getItemsForSelectedTask() {
    if(this.selectedTab !== null && 
      this.selectedTab !== undefined) {
        console.log('selectedtab',this.selectedTab, this.selectedTasks)
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
      this.updatedItemsFromTasks.emit(this.items);
      console.log('in delete items', this.selectedTasks[this.selectedTab].items)
    }
  }
}
