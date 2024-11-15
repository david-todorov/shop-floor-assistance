import { Component } from '@angular/core';
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
export class SuggestionsComponent {

  constructor(private suggestionService: SuggestionsService){}
  
  drop(event:  CdkDragDrop<itemTO[]>) {
    
    // if (event.previousContainer === event.container) {
    //   debugger;
    //   moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    // } else {
    //   transferArrayItem(
    //     event.previousContainer.data,
    //     event.container.data,
    //     event.previousIndex,
    //     event.currentIndex
    //   );
    // }
    const index = event.previousIndex;
    this.suggestionService.triggerDrop(event,index);
    console.log('service dispatched in suggestionscomponent with index', index)
  }






  elementSelected: string= 'Workflows';
  elementsOffered: string[] = ['Workflows', 'Tasks', 'Items'];

  // order: orderTO= dummyOrder;
  workflows: workflowTO[]= sampleWorkflows;
  tasks: taskTO[]= sampleTasks;
  items_sugg: itemTO[]= sampleItems;

}
