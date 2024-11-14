import { Component, Input } from '@angular/core';
import { MatListModule } from '@angular/material/list';
import { ButtonComponent } from '../button/button.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [MatListModule,
    ButtonComponent,
    CommonModule],
  templateUrl: './list.component.html',
  styleUrl: './list.component.css'
})
export class ListComponent {
  @Input() items: string[] = [];

}