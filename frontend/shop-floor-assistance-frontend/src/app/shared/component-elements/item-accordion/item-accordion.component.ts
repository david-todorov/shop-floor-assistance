import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
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

deleteWorkflow(_t4: number,$event: MouseEvent) {
// throw new Error('Method not implemented.');
}
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

  expandedPanels: boolean[] = [];
  doneAll: boolean= true;

  items!:itemTO[];
  selectedItem!: number | null;

  ngOnChanges(changes: SimpleChanges): void {
    console.log('in item-accord-change')
    if(changes['selectedTasks'] || changes['selectedTab']){
      if(this.selectedTab != null && this.selectedTab >=0)
        this.items= this.selectedTasks[this.selectedTab].items;
    }
  }

  onAccordionClick(index: number): void {
    // console.log('Accordion element clicked:', index);
    // this.selectedItem= index;
  }
}
