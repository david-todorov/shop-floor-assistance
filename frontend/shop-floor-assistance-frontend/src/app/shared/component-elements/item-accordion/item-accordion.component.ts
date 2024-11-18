import { ChangeDetectorRef, Component, EventEmitter, input, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { itemTO } from '../../../types/itemTO';
import { taskTO } from '../../../types/taskTO';
import { MatAccordion, MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { itemUIStates } from '../workflowUI-state';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { orderTO } from '../../../types/orderTO';
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
export class ItemAccordionComponent implements OnInit, OnChanges, OnDestroy{

  @Input() order!:orderTO;
  @Input() workflowIndex!:number | null;
  @Input() taskIndex!: number | null;
  
  @Output() onOrderUpdate= new EventEmitter<orderTO>();

  expandedPanels: boolean[] = [];
  items!:itemTO[];
  btnLabelAddItem: string= 'Add Item'


  //-Helper variables to maintain UI state
  itemUIStates: itemUIStates= {};
  itemIndices: {[workflowIndex: number]:{ [taskIndex: number]: number } } = {};
  itemsCheckStatuses: {[workflow: number]: { [task: number]: {[item: number]: boolean}}} = {};
  @ViewChild(MatAccordion) accordion!: MatAccordion;

  @Input() dropItemIdFromTask: string='itemsInTasks';
  dropItemIds: string[] = [];

  //-----------------------------------------

  constructor(private cdr:ChangeDetectorRef, 
    private uiService: UIService,
    private suggestionService: SuggestionsService,
    private snackBar: MatSnackBar){}

  //--life cycle hooks
  ngOnInit(): void {
    this.itemIndices = this.uiService.getItemIndices();
    if (Object.keys(this.itemIndices).length === 0) {
      this.initializeItemIndices();
    }
    this.itemsCheckStatuses = this.uiService.getItemStatuses();
    if (Object.keys(this.itemsCheckStatuses).length === 0) {
      this.initializeCheckStatuses();
    }
    // this.dropItemIds.push(this.dropItemIdFromTask);
    this.suggestionService.addDropItemId(this.dropItemIdFromTask);
  }


  ngOnDestroy(): void {
    this.uiService.setItemIndices(this.itemIndices);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['workflowIndex'] ){
      this.closeAllPanels();
    }
    if(changes['order'] || changes['workflowIndex'] || changes['taskIndex'] || changes['itemIndex']){
      if(this.taskIndex != null && this.taskIndex >=0){
        this.getItemsForSelectedTask();
        this.initializeItemUIStates(); 

        this.cleanUpCheckStatuses();
        this.cleanUpItemIndices();
   
   }
    }
  }
  //---------------------------------------------------------------
  //--Ui helper functions for maintaing itemindices and checkstatuses
  initializeItemIndices(): void {
    if (this.order && this.order.workflows) {
      this.order.workflows.forEach((workflow, workflowIndex) => {
        workflow.tasks.forEach((task, taskIndex) => {
          this.uiService.setSelectedItemIndex(workflowIndex, taskIndex, 0);
        });
      });
    }
  }

  initializeCheckStatuses(): void {
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

  cleanUpCheckStatuses(): void {
    this.itemsCheckStatuses = this.uiService.getItemStatuses();
    const newCheckStatuses: { [workflowIndex: number]: { [taskIndex: number]: { [itemIndex: number]: boolean } } } = {};
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
      this.items = [...task.items]; // Create a new array reference
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
      this.items = [...task.items]; // Create a new array reference
      this.initializeItemUIStates(); 
      // this.expandedPanels= new Array(this.items.length).fill(false);
      this.closeAllPanels();
      this.onOrderUpdate.emit(this.order);
    }
  }

  saveItems(index: number){
    if(this.itemUIStates[index].updatedTitle=='' || this.itemUIStates[index].updatedTitle==null){
      alert('The item name cannot be empty!');
      return;
    }
    this.items[index].name= this.itemUIStates[index].updatedTitle;
    this.items[index].description= this.itemUIStates[index].updatedDescription;
    this.items[index].timeRequired= this.itemUIStates[index].upDatedTimeReq;
  }

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
    console.log('checkstatuses',this.itemsCheckStatuses);
    if(Object.values(this.itemsCheckStatuses[workflowIndex][taskIndex]).every(status=>status===true)){
      console.log('All checked in at', this.order.workflows[workflowIndex].tasks[taskIndex].name);
    }else{
       console.log('Not all checked at', this.order.workflows[workflowIndex].tasks[taskIndex].name);
    }
  }
  
  closeAllPanels(): void {
    if (this.accordion) {
      this.accordion.closeAll();
    }
  }

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

  showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }

}
