import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { ButtonComponent } from '../button/button.component';

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [CommonModule, MatListModule, ButtonComponent],
  templateUrl: './list.component.html',
  styleUrl: './list.component.css'
})
export class ListComponent {
  @Input() workflows: any[] | undefined;
  @Input() selectedWorkflow: any;
  @Output() workflowSelected = new EventEmitter<any>();
}
