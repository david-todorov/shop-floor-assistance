import { Component, OnInit } from '@angular/core';
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
  receivedItemIds: string[]=[];

  constructor(private suggestionService: SuggestionsService){}

  ngOnInit(): void {
    this.receivedItemIds= this.suggestionService.getDropListIds();
  }
  
  drop(event: CdkDragDrop<itemTO[]> | CdkDragDrop<workflowTO[]>) {
    if (event.previousContainer === event.container){
      // moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
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
