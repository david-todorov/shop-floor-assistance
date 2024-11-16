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
import { BackendCommunicationService } from '../../../services/backend-communication.service';
import { catchError, map, of } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { productTO } from '../../../types/productTO';

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

  onElementSelected(selectedElement: string): void {
    switch (selectedElement) {
      case 'Workflows':
        this.getWorkflowSuggestions();
        break;
      case 'Tasks':
        this.getTaskSuggestions();
        break;
      case 'Items':
        this.getItemSuggestions();
        break;
      default:
        console.warn('Unknown element selected:', selectedElement);
    }
  }

  getWorkflowSuggestions(): void {
    console.log('productafter in suggestions',this.productAfter)
    this.backendCommunicationService.getWorkflowSuggestions(this.productAfter).pipe(
      map(response => {
        if (response.status === 200) {
          return response.body;
        } else {
          throw new Error('Unexpected response status: ' + response.status);
          // alert('Error retrieving suggestions: '+response.status);
        }
      }),
      catchError(err => {
        console.error('Error getting workflow suggestions:', err);
        return of(null);
      })
    ).subscribe({
      next: (response: any) => {
        if (response) {
          // this.workflows_suggestions = response;
          console.log('Workflow suggestions:', this.workflows_suggestions);
        }
      },
      error: (err) => {
        console.error('An error occurred while retrieving workflow suggestions:', err);
      }
    });
  }

  getTaskSuggestions(): void {
    console.log('productafter in suggestions',this.productAfter)
    this.backendCommunicationService.getTaskSuggestions(this.productAfter).pipe(
      map(response => {
        if (response.status === 200) {
          return response.body;
        } else {
          throw new Error('Unexpected response status: ' + response.status);
          // alert('Error retrieving suggestions: '+response.status);
        }
      }),
      catchError(err => {
        console.error('Error getting task suggestions:', err);
        return of(null);
      })
    ).subscribe({
      next: (response: any) => {
        if (response) {
          // this.workflows_suggestions = response;
          console.log('Item suggestions:', this.items_suggestions);
        }
      },
      error: (err) => {
        console.error('An error occurred while retrieving task suggestions:', err);
      }
    });
  }

  getItemSuggestions(): void {
    console.log('productafter in suggestions',this.productAfter)
    this.backendCommunicationService.getItemSuggestions(this.productAfter).pipe(
      map(response => {
        if (response.status === 200) {
          return response.body;
        } else {
          throw new Error('Unexpected response status: ' + response.status);
          // alert('Error retrieving suggestions: '+response.status);
        }
      }),
      catchError(err => {
        console.error('Error getting item suggestions:', err);
        return of(null);
      })
    ).subscribe({
      next: (response: any) => {
        if (response) {
          // this.workflows_suggestions = response;
          console.log('Item suggestions:', this.items_suggestions);
        }
      },
      error: (err) => {
        console.error('An error occurred while retrieving item suggestions:', err);
      }
    });
  }

// onElementSelected(event: any) {
//   console.log(event);
//    this.backendCommunicationService.getWorkflowSuggestions().pipe(
//       catchError((err) => {
//         console.error('Error fetching equipment:', err);
//         return of([]);
//       })
//     ).subscribe({
//       next: (response: any) => {
//         // this.workflows_suggestions = response; 
//         console.log(response)
//         console.log('Equipment loaded:', this.workflows_suggestions);
//       },
//       error: (err) => {
//         console.error('An error occurred while retreiving workflow suggestions:', err);
//         return;
//       }
//     });
// }








  receivedItemIds: string[]=[];
  receivedTaskIds: string[]=[];
  id_container: string= Global.task_suggestions_container_id;
  @Input() productAfter!: productTO;

  constructor(private suggestionService: SuggestionsService,
              private backendCommunicationService: BackendCommunicationService,
            private http: HttpClient){}

  ngOnInit(): void {
    console.log('product after is', this.productAfter)
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

 
  workflows_suggestions!: workflowTO[];
  tasks_suggestions!: taskTO[];
  items_suggestions!: itemTO[];

}
