import { Component, Input, input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatRadioModule } from '@angular/material/radio';
import { workflowTO } from '../../../types/workflowTO';
import { orderTO } from '../../../types/orderTO';
// import { dummyOrder } from '../../../types/dummyData';
import { taskTO } from '../../../types/taskTO';
import { itemTO } from '../../../types/itemTO';
import { MatAccordion, MatExpansionModule } from '@angular/material/expansion';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { SuggestionsService } from '../../../services/suggestions.service';
import { itemDropEvent } from '../../../types/itemDropEventType';
import { CommonModule } from '@angular/common';
import { dummyOrder, sampleItems, sampleTasks, sampleWorkflows } from '../../../types/dummyData';
import { Global } from '../../../services/globals';

@Component({
  selector: 'app-suggestions',
  standalone: true,
  imports: [
    MatRadioModule,
    FormsModule,
    MatExpansionModule,
    DragDropModule,
    CommonModule
  ],
  templateUrl: './suggestions.component.html',
  styleUrl: './suggestions.component.css'
})
export class SuggestionsComponent implements OnInit{
onElementSelected(event: any) {
  console.log(event)
}
  receivedItemIds: string[]=[];
  receivedTaskIds: string[]=[];
  id_container: string= Global.task_suggestions_container_id;
  @Input() orderId!: number;

  constructor(private suggestionService: SuggestionsService){}

  ngOnInit(): void {
    console.log('order id is', this.orderId)
    this.receivedItemIds= this.suggestionService.getDropItemIds();
    this.receivedTaskIds= this.suggestionService.getDropTaskIds();
  }
  
  drop(event: CdkDragDrop<itemTO[]> | CdkDragDrop<workflowTO[]> | CdkDragDrop<taskTO[]>) {
    if (event.previousContainer === event.container){
      const item = event.container.data[event.previousIndex];
      event.container.data.splice(event.previousIndex, 1);
      event.container.data.splice(event.currentIndex, 0, item);
    }
  }

  elementSelected: string= 'Workflows';
  elementsOffered: string[] = ['Workflows', 'Tasks', 'Items'];

 
  workflows_suggestions: workflowTO[]= sampleWorkflows;
  tasks_suggestions: taskTO[]= sampleTasks;
  items_suggestions: itemTO[]= sampleItems;

}
