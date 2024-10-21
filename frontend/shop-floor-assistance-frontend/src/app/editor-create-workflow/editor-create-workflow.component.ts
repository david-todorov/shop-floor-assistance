import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTabChangeEvent, MatTabsModule } from '@angular/material/tabs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { workflowTO } from '../types/workflowTO';
import { taskTO } from '../types/taskTO';
import { itemTO } from '../types/itemTO';
import { orderTO } from '../types/orderTO';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';


@Component({
  selector: 'app-editor-create-workflow',
  standalone: true,
  imports: [  MatFormFieldModule,
    MatInputModule,
    MatTabsModule,
    MatIconModule,
    FormsModule,
    CommonModule,],
  templateUrl: './editor-create-workflow.component.html',
  styleUrl: './editor-create-workflow.component.css'
})
export class EditorCreateWorkflowComponent {

  orderName: string = '';
  workflows: workflowTO[] = [];

  constructor() {}

  tabs = [
    { label: 'Tab 1', content: 'Tab 1 Content' }
  ];
  selectedIndex = 0;

  addTab(event: MouseEvent) {
    event.preventDefault(); // Prevent the default tab switch behavior
    const newIndex = this.tabs.length + 1;
    this.tabs.push({ label: `Tab ${newIndex}`, content: `Tab ${newIndex} Content` });
    this.selectedIndex = this.tabs.length; // Set the selected index to the new tab
  }
}


