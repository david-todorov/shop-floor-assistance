import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardComponent } from '../components/card/card.component';
import { ListComponent } from '../components/list/list.component';
import { TabsComponent } from '../components/tabs/tabs.component';
import { ButtonComponent } from '../components/button/button.component';
import { MatTabsModule } from '@angular/material/tabs';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Workflow, OrderTO, dummyOrder, Task, Item } from '../types/dummyData';

@Component({
  selector: 'app-operator-assistance',
  standalone: true,
  imports: [
    ButtonComponent, 
    TabsComponent, 
    CommonModule,  
    ListComponent, 
    FormsModule, 
    CardComponent,
    MatTabsModule,
    MatCheckboxModule,
    MatExpansionModule],
  templateUrl: './operator-assistance.component.html',
  styleUrl: './operator-assistance.component.css'
})

export class OperatorAssistanceComponent implements OnInit {
  order: OrderTO = dummyOrder;
  selectedWorkflow: any;
  selectedTaskIndex: number = 0;

  ngOnInit(): void {
      
  }

  selectWorkflow(workflow: Workflow): void {
    this.selectedWorkflow = workflow;
  }

   // Check if all items in the current task are completed
  canProceedToNextTask(): boolean {
  const currentTask: Task | undefined = this.selectedWorkflow?.tasks[this.selectedTaskIndex];

  // Explicitly define the type of item as `Item`
  return currentTask ? currentTask.items.every((item: Item) => item.completed) : false;
}

  // Move to the next task (next tab)
  goToNextTask(): void {
    if (this.selectedTaskIndex < this.selectedWorkflow.tasks.length - 1) {
      this.selectedTaskIndex++;  // Move to the next task (next tab)
    }
  }
}
