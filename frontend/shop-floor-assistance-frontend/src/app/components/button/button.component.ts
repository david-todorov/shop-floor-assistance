import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [MatButtonModule],
  templateUrl: './button.component.html',
  styleUrl: './button.component.css'
})
export class ButtonComponent {
  @Input() label: string = "Button";
  @Input() color: string = 'primary';
  @Input() ngClass: { [key: string]: boolean } = {};

  @Output() click = new EventEmitter<void>;
}
