import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [MatButtonModule],
  templateUrl: './button.component.html',
  styleUrl: './button.component.css'
})
export class ButtonComponent {

  @Input() label!: string;
  @Input() disabled!: boolean;
  @Output() onClick = new EventEmitter<any>();

  onClickButton(event: MouseEvent) {
      this.onClick.emit(event);
    }
}
