import { ChangeDetectorRef, Component, EventEmitter, input, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { itemTO } from '../../types/itemTO';
import { taskTO } from '../../types/taskTO';
import { MatAccordion, MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { itemCheckStatuses, itemIndices, itemUIStates, taskCheckStatuses } from '../../types/workflowUI-state';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { orderTO } from '../../types/orderTO';
import { UIService } from '../../../services/ui.service';
import { ButtonComponent } from '../button/button.component';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { SuggestionsService } from '../../../services/suggestions.service';
import { MatSnackBar } from '@angular/material/snack-bar';

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
    MatCheckboxModule,
    ButtonComponent,
    DragDropModule
  ],
  templateUrl: './item-accordion.component.html',
  styleUrl: './item-accordion.component.css'
})
 /**
   * Item accordion
   * 
   * This file implements the accordion element which is used to display items. It is highly interactive,
   * supporting reordering and draging of items to and from the component. The accordionn can also 
   * be expanded to display longer descriptions as well.
   * It also supports editing of items and deletion of items from the parent workflow.
   * @author Jossin Antony
*/
export class ItemAccordionComponent implements OnInit, OnChanges, OnDestroy{

  //Receive inputs from parent component
  @Input() order!:orderTO;
  @Input() workflowIndex!:number | null;
  @Input() taskIndex!: number | null;
  @Input() isEditorMode!: boolean;
  
  //Emit events to parent component
  @Output() onOrderUpdate= new EventEmitter<orderTO>();
  @Output() onItemsChecked= new EventEmitter<itemCheckStatuses>();

  expandedPanels: boolean[] = [];
  items!:itemTO[];
  btnLabelAddItem: string= 'Add Item'


  //-Helper variables to maintain UI state
  itemUIStates: itemUIStates= {};
  itemIndices: itemIndices= {};
  itemsCheckStatuses: itemCheckStatuses = {};
  tasksCheckStatuses: taskCheckStatuses = {};

  @ViewChild(MatAccordion) accordion!: MatAccordion;

  @Input() dropItemIdFromTask: string='itemsInTasks';
  dropItemIds: string[] = [];

  //-----------------------------------------

  constructor(private cdr:ChangeDetectorRef, 
    private uiService: UIService,
    private suggestionService: SuggestionsService,
    private snackBar: MatSnackBar){}

  //--life cycle hooks, initiallze indices and check statuses.
  ngOnInit(): void {
    this.itemIndices = this.uiService.getItemIndices();
    if (Object.keys(this.itemIndices).length === 0) {
      this.initializeItemIndices();
    }
    this.itemsCheckStatuses = this.uiService.getItemStatuses();
    if (Object.keys(this.itemsCheckStatuses).length === 0) {
      this.initializeItemCheckStatuses();
    }
    if (Object.keys(this.tasksCheckStatuses).length === 0) {
      this.initializeTaskCheckStatuses();
    }
    this.suggestionService.addDropItemId(this.dropItemIdFromTask);
  }


  ngOnDestroy(): void {
    this.uiService.setItemIndices(this.itemIndices);
  }

  //When change is detected
  ngOnChanges(changes: SimpleChanges): void {
    if(changes['workflowIndex'] ){
      this.closeAllPanels();
    }
    if(changes['order'] || changes['workflowIndex'] || changes['taskIndex'] || changes['itemIndex']){
      if(this.taskIndex != null && this.taskIndex >=0){
        this.getItemsForSelectedTask();
        this.initializeItemUIStates(); 

        this.cleanUpItemCheckStatuses();
        this.cleanUpTaskCheckStatuses();
        this.cleanUpItemIndices();
   }
    }
  }
  //---------------------------------------------------------------
  //--Ui helper functions for initializing and maintaining itemindices and checkstatuses.
   /**
   * Checkstatus:
   * The fiunctionality where completed elements ar indicated by a UI symbol, e.g, a green tick on the screen 
   * as a visual indicator for the user about the completion of tasks.
   * I.e, for an item -> when the item is marked as done by operator
   * for a task tab -> When all its children items are marked as checked
   * for a workflow -> when all task tabs are marked as done
   * for an order -> when all workflows have been marked as done.
   * Note: This functionality has not been implemented completely. Currently, only items 
   * show the green tick when checked.
   * The check statuses have to be propoagated back to its parent components and up through the
   * hierarchy in order to complete the implementaion.
*/
  initializeItemIndices(): void {
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        workflow.tasks.forEach((task, taskIndex) => {
          this.uiService.setSelectedItemIndex(workflowIndex, taskIndex, 0);
        });
      });
    }
  }

  initializeItemCheckStatuses(): void {
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        workflow.tasks.forEach((task, taskIndex) => {
          task.items.forEach((item, itemIndex) => {
            this.itemsCheckStatuses[workflowIndex] = this.itemsCheckStatuses[workflowIndex] || {};
            this.itemsCheckStatuses[workflowIndex][taskIndex] = this.itemsCheckStatuses[workflowIndex][taskIndex] || {};
            this.itemsCheckStatuses[workflowIndex][taskIndex][itemIndex] = false;
            this.uiService.setItemStatuses(this.itemsCheckStatuses);
          });
        });
      });
    }
  }

  initializeTaskCheckStatuses(): void {
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        workflow.tasks.forEach((task, taskIndex) => {
            this.tasksCheckStatuses[workflowIndex] = this.tasksCheckStatuses[workflowIndex] || {};
            this.tasksCheckStatuses[workflowIndex][taskIndex] =  false;
            this.uiService.setTaskStatuses(this.tasksCheckStatuses);
        });
      });
    }
  }

  cleanUpItemCheckStatuses(): void {
    this.itemsCheckStatuses = this.uiService.getItemStatuses();
    const newCheckStatuses: itemCheckStatuses = {};
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        newCheckStatuses[workflowIndex] = {};
        workflow.tasks.forEach((task, taskIndex) => {
          newCheckStatuses[workflowIndex][taskIndex] = {};
          task.items.forEach((item, itemIndex) => {
            if (this.itemsCheckStatuses[workflowIndex] && this.itemsCheckStatuses[workflowIndex][taskIndex] && this.itemsCheckStatuses[workflowIndex][taskIndex][itemIndex] !== undefined) {
              newCheckStatuses[workflowIndex][taskIndex][itemIndex] = this.itemsCheckStatuses[workflowIndex][taskIndex][itemIndex];
            } else {
              newCheckStatuses[workflowIndex][taskIndex][itemIndex] = false;
            }
          });
        });
      });
    }
    this.itemsCheckStatuses = newCheckStatuses;
    this.uiService.setItemStatuses(this.itemsCheckStatuses);
  }

  cleanUpTaskCheckStatuses(): void {
    this.tasksCheckStatuses = this.uiService.getTaskStatuses();
    const newCheckStatuses: taskCheckStatuses = {};
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        newCheckStatuses[workflowIndex] = {};
        workflow.tasks.forEach((task, taskIndex) => {
            if (this.tasksCheckStatuses[workflowIndex] && this.tasksCheckStatuses[workflowIndex][taskIndex] !== undefined) {
              newCheckStatuses[workflowIndex][taskIndex] = this.tasksCheckStatuses[workflowIndex][taskIndex];
            } else {
              newCheckStatuses[workflowIndex][taskIndex] = false;
            }
        });
      });
    }
    this.tasksCheckStatuses = newCheckStatuses;
    this.uiService.setTaskStatuses(this.tasksCheckStatuses);
  }

  cleanUpItemIndices(): void {
    this.itemIndices = this.uiService.getItemIndices();
    const newItemIndices: { [workflowIndex: number]: { [taskIndex: number]: number } } = {};
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        newItemIndices[workflowIndex] = {};
        workflow.tasks.forEach((task, taskIndex) => {
          if (this.itemIndices[workflowIndex] && this.itemIndices[workflowIndex][taskIndex] !== undefined) {
            newItemIndices[workflowIndex][taskIndex] = this.itemIndices[workflowIndex][taskIndex];
          } else {
            newItemIndices[workflowIndex][taskIndex] = 0;
          }
        });
      });
    }
    this.itemIndices = newItemIndices;
    this.uiService.setItemIndices(this.itemIndices);
  }

  //----------------------------------------

  getItemsForSelectedTask() {
    if(this.taskIndex != null && this.workflowIndex !=null){
      const task= this.order.workflows[this.workflowIndex].tasks[this.taskIndex]
      this.itemIndices= this.uiService.getItemIndices();
      this.items = [...task.items]; // Create a new array reference for triggering ngOnChange
    }else{
      this.items=[];
    }
  }

  initializeItemUIStates() {//keep the itemUIStates in tandem with item update/delete/ etc.
    this.items.forEach((item: itemTO, index: number) => {
      this.itemUIStates[index] = { editMode: false, 
        showDescription: false, 
        updatedTitle: item.name,
        updatedDescription: item.description,
        upDatedTimeReq: item.timeRequired};
    });
  }

  //Deletion of items
  deleteItems(index: number,event: MouseEvent) {
    if(this.taskIndex!=null && this.workflowIndex !=null){
      event.stopPropagation();
      const task= this.order.workflows[this.workflowIndex].tasks[this.taskIndex];
      task.items.splice(index, 1);
      // Update checkStatuses
      delete this.itemsCheckStatuses[this.workflowIndex][this.taskIndex][index];
      // Update itemIndices array
      this.itemIndices[this.workflowIndex][this.taskIndex] = task.items.length > 0 ? 0 : -1;
      this.uiService.setItemIndices(this.itemIndices);
      this.items = [...task.items]; // Create a new array reference to trigger ngOnchange
      this.initializeItemUIStates(); 
      this.closeAllPanels();
      this.onOrderUpdate.emit(this.order);
    }
  }

  //Saving the edited information.
  saveItems(index: number){
    if(this.itemUIStates[index].updatedTitle=='' || this.itemUIStates[index].updatedTitle==null){
      alert('The item name cannot be empty!');
      return;
    }
    this.items[index].name= this.itemUIStates[index].updatedTitle;
    this.items[index].description= this.itemUIStates[index].updatedDescription;
    this.items[index].timeRequired= this.itemUIStates[index].upDatedTimeReq;
  }

  //Enter adn leave edit mode. Expand panels if in edit mode
  toggleEditMode(index: number, event: MouseEvent) {
    event.stopPropagation();
    this.expandedPanels[index] = !this.expandedPanels[index]; // Expand the panel
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
  }

  isAnyWorkflowInEditMode(): boolean {
    return Object.values(this.itemUIStates).some(state => state.editMode);
  }

  updateCheckStatus(workflowIndex: number, taskIndex: number, itemIndex: number, checked: boolean): void {
    this.itemsCheckStatuses[workflowIndex][taskIndex][itemIndex] = checked;
    this.uiService.setSelectedItemStatus(workflowIndex, taskIndex, itemIndex, checked);
    const taskChecked= Object.values(this.itemsCheckStatuses[workflowIndex][taskIndex]).every(status=>status===true);
    this.tasksCheckStatuses[workflowIndex][taskIndex]=taskChecked;
    this.onItemsChecked.emit(this.itemsCheckStatuses)
  }
  
  closeAllPanels(): void {
    if (this.accordion) {
      this.accordion.closeAll();
    }
  }

  //Add a new item with default text.
  resolveAddItem(event: any) {
    if(this.workflowIndex!=null && this.taskIndex!=null){
      event.stopPropagation();
      const newItem: itemTO= { 
        name: 'New Item', 
        description: 'Item description', 
        timeRequired: null 
      };
      this.order.workflows[this.workflowIndex].tasks[this.taskIndex].items.push(newItem);
      console.log(this.order)
      this.order= {...this.order};
      this.onOrderUpdate.emit(this.order);
    }
  }

  //Drop-action to the accordion
  drop(event: CdkDragDrop<itemTO[]>): void {
    if(this.workflowIndex!=null && this.taskIndex!=null){
      const items= this.order.workflows[this.workflowIndex].tasks[this.taskIndex].items
      if (event.previousContainer === event.container) {
        moveItemInArray(items, event.previousIndex, event.currentIndex);
      }else{
        transferArrayItem(
          event.previousContainer.data,
          items,
          event.previousIndex,
          event.currentIndex
        );
        this.showSnackbar('New item added to task!');
      }
    }
  this.onOrderUpdate.emit(this.order);
  }

  //Snackbar to show the user about the element drop.
  showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }

}
