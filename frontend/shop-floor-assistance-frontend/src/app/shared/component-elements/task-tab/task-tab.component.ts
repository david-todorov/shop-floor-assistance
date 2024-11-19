import { AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, QueryList, SimpleChanges, ViewChild, ViewChildren } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTabGroup, MatTabsModule } from '@angular/material/tabs';
import { orderTO } from '../../types/orderTO';
import { CommonModule } from '@angular/common';
import { taskTO } from '../../types/taskTO';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EditTaskDialogComponent } from '../edit-task-dialog/edit-task-dialog.component';
import { ItemAccordionComponent } from '../item-accordion/item-accordion.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { SuggestionsService } from '../../../services/suggestions.service';
import { Global } from '../../../services/globals';
import { ButtonComponent } from '../button/button.component';
import { itemCheckStatuses, taskCheckStatuses } from '../../types/workflowUI-state';

@Component({
  selector: 'app-task-tab',
  standalone: true,
  imports: [
    MatIconModule,
    MatTabsModule,
    CommonModule,
    MatDialogModule,
    ItemAccordionComponent,
    DragDropModule,
    ButtonComponent
  ],
  templateUrl: './task-tab.component.html',
  styleUrl: './task-tab.component.css'
})
export class TaskTabComponent implements OnInit, OnChanges, AfterViewInit{
onItemsChecked(event: itemCheckStatuses) {
  this.onItemsCheckedReceived.emit(event);
}

  status!:taskCheckStatuses;

  updateTasksInOrder(event: taskCheckStatuses){
    this.status= event;
  }

  @Output() onItemsCheckedReceived = new EventEmitter<itemCheckStatuses>();


  @Input() order!: orderTO;
  @Input() workflowIndex!: number | null;
  @Input() isEditorMode!: boolean;
  
  @Output() onOrderUpdate = new EventEmitter<orderTO>();
  @Output() onWorkflowChange = new EventEmitter<number | null>();
  @Output() ontaskChange = new EventEmitter<number | null>();
  @ViewChild(MatTabGroup) tabGroup!: MatTabGroup;

  taskIndex: number | null = 0;
  orderExists: boolean= false;
  tasks!: taskTO[];
  btnLabelAddTask: string= 'Add task';
  itemsInTasks: string='itemsInTasks';

  constructor(public dialog: MatDialog,
              private cdr:ChangeDetectorRef,
              private snackBar: MatSnackBar,
              private suggestionService: SuggestionsService,
  ){}

  ngOnInit(): void {
    if (this.workflowIndex == null) {
      this.workflowIndex = 0;
      this.taskIndex = 0;
      this.setTabGroupIndex(0);
    }
  //      this.taskElements.forEach((taskElement, index) => {
  //   const id = taskElement.nativeElement.id;
  //   console.log(`Task element ID for index ${index}:`, id);
  // });
  }

  ngAfterViewInit(): void {
      // setTimeout(() => {
      this.setTabGroupIndex(0);
    // })
    //  this.tasks.forEach((_, i) => {
    //   const dropTaskId = `task-${i}`;
    //   this.suggestionService.addDropTaskId(dropTaskId);
    // });
    // console.log('all tabs,', this.suggestionService.getDropTaskIds())
    // this.taskElements.forEach((taskElement, index) => {
    // const dropTaskId = taskElement.nativeElement.id;
    // // console.log(`Task element ID for index ${index}:`, dropTaskId);
    // this.suggestionService.addDropTaskId(dropTaskId);
  // });
  }



  async setTabGroupIndex(index: number): Promise<void> {
    await this.waitForTabGroup();
    this.tabGroup.selectedIndex = index;
    this.cdr.detectChanges();
  }

  private waitForTabGroup(): Promise<void>{
    return new Promise((resolve)=>{
      if(this.tabGroup){
        resolve();
      }else{
        const interval= setInterval(()=>{
          if(this.tabGroup){
            clearInterval(interval);
            resolve();
          }
        },50);
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['workflowIndex']) {
      this.taskIndex = 0;
      this.onWorkflowChange.emit(this.workflowIndex);
    }
    if (changes['workflowIndex'] || changes['order'] || changes['taskIndex']) {
      this.getTasksForSelectedWorkflow();
      console.log('Relevant changes detected:', changes);
    }
  }

  getTasksForSelectedWorkflow() {
    if(this.workflowIndex != null && this.order.workflows) {
      this.tasks = [...this.order.workflows[this.workflowIndex].tasks];
    }else{
      this.tasks=[];
    }
  }

  deleteTasks(index: any,event: MouseEvent) {
    if (this.workflowIndex !== null) {
      event.stopPropagation();
      this.order.workflows[this.workflowIndex].tasks.splice(index, 1);
      if(index>=this.order.workflows[this.workflowIndex].tasks.length && this.taskIndex !=null){
        this.taskIndex-=1;
      }
      this.tasks = [...this.order.workflows[this.workflowIndex].tasks];
      this.onOrderUpdate.emit(this.order);
    }
  }

  editTask(task: taskTO) {
    const dialogRef = this.dialog.open(EditTaskDialogComponent, {
      width: '750px',
      data: { ...task },
      panelClass: 'custom-dialog-container' 
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && this.workflowIndex!=null && this.taskIndex!=null) {
        this.order.workflows[this.workflowIndex].tasks[this.taskIndex].name= result.taskname;
        this.order.workflows[this.workflowIndex].tasks[this.taskIndex].description= result.description;
      }
      this.onOrderUpdate.emit(this.order);
    });

}

  updateItemsInOrder(event: orderTO){
    // if(this.workflowIndex!=null && this.taskIndex!=null){
      this.onOrderUpdate.emit(this.order);
    // }
  }

  onTaskSelected(taskIndex: number) {
    this.ontaskChange.emit(taskIndex);
  }

    resolveAddTAsk(event: any) {
    if(this.workflowIndex!=null){
      event.stopPropagation();
      const newTask: taskTO= {
        name: 'New Task', 
        description: 'Task Description', 
        items: [
          // { name: 'New Item', 
          //   description: 'Item Description', 
          //   timeRequired: null }
            { 
              name: 'New Item', 
              description: 'Item Description',
              timeRequired: null 
             }
          ] 
        };
      this.order.workflows[this.workflowIndex].tasks.push(newTask);
      console.log(this.order)
      this.order= {...this.order};
      this.onOrderUpdate.emit(this.order);
      this.tabGroup.selectedIndex = 0;
      this.showSnackbar('New task appended to the end of the workflow!');
    }
  }

  showSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 1500
    });
  }

  // ---Drag functions--------------------
  drop(event: CdkDragDrop<taskTO[]>){
    if(this.workflowIndex!=null){
      let currentIndex = parseInt(event.previousContainer.id.replace("task-",""));
      let targetIndex = parseInt(event.container.id.replace("task-",""));
      let sourceArray!: taskTO[]; 
      let sourceIndex!: number;

      const tasks= this.order.workflows[this.workflowIndex].tasks

      if(event.previousContainer.id=== Global.task_suggestions_container_id){
        sourceArray= event.previousContainer.data;
        sourceIndex= event.previousIndex;
      }else{
        sourceArray= tasks;
        sourceIndex= currentIndex;
      }
      transferArrayItem(sourceArray, tasks,sourceIndex,targetIndex);
      
      // if(event.previousContainer.id=== Global.task_suggestions_container_id){
      //    transferArrayItem(
      //       event.previousContainer.data,
      //       tasks,
      //       event.previousIndex,
      //       targetIndex);
      // }else{
      //   transferArrayItem(
      //     tasks,
      //     tasks,
      //     currentIndex,
      //     targetIndex);}
      //   }
      }
    this.onOrderUpdate.emit(this.order);
  }


 getAllDragTabs(index:number){
    let taskList = []
    for(let i=0;i<this.tasks.length;i++){
      if(i!=index){
        taskList.push("task-"+i);
        this.suggestionService.addDropTaskId("task-"+i)
      }
    }
    // this.suggestionService.setDropTaskIdArray(taskList);
    // console.log('in tasklist in tabsss: tasklist', this.suggestionService.getDropTaskIds())
    return taskList;
  }
// ----------------------------------------------


}
