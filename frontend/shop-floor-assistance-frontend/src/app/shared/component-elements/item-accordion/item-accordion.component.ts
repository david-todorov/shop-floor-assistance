import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { itemTO } from '../../../types/itemTO';
import { taskTO } from '../../../types/taskTO';

@Component({
  selector: 'app-item-accordion',
  standalone: true,
  imports: [],
  templateUrl: './item-accordion.component.html',
  styleUrl: './item-accordion.component.css'
})
export class ItemAccordionComponent implements OnChanges{


  @Input() selectedTasks!: taskTO[];
  @Input() selectedTab!: number | null;


  ngOnChanges(changes: SimpleChanges): void {
  }
}
