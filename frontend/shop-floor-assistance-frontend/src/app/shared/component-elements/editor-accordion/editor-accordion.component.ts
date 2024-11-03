import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';

@Component({
  selector: 'app-editor-accordion',
  standalone: true,
  imports: [ MatExpansionModule,
    MatButtonModule,
  CommonModule],
  templateUrl: './editor-accordion.component.html',
  styleUrl: './editor-accordion.component.css'
})
export class EditorAccordionComponent {
  

}
