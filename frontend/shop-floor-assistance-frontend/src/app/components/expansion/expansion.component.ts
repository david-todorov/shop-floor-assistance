import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatExpansionModule } from '@angular/material/expansion';
import { CheckboxComponent } from '../checkbox/checkbox.component';

@Component({
  selector: 'app-expansion',
  standalone: true,
  imports: [CommonModule, MatExpansionModule, CheckboxComponent],
  templateUrl: './expansion.component.html',
  styleUrl: './expansion.component.css'
})

export class ExpansionComponent {
  @Input() task: any;
}
