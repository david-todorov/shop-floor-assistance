import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { ExpansionComponent } from '../expansion/expansion.component';
import { CheckboxComponent } from '../checkbox/checkbox.component';
@Component({
  selector: 'app-tabs',
  standalone: true,
  imports: [
    CommonModule, 
    MatTabsModule, 
    ExpansionComponent,
    CheckboxComponent],
  templateUrl: './tabs.component.html',
  styleUrl: './tabs.component.css'
})
export class TabsComponent {
  @Input() selectedWorkflow: any;
}
