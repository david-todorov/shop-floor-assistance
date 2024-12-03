import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-button', // Selector for the button component
  standalone: true, // Indicates this component is standalone
  imports: [MatButtonModule], // Imports Angular Material Button Module
  templateUrl: './button.component.html', // Path to the component's HTML template
  styleUrl: './button.component.css' // Path to the component's CSS styles
})
export class ButtonComponent {

  @Input() label!: string; // Input property to receive the button label
  @Input() disabled!: boolean; // Input property to control the disabled state of the button
  @Output() onClick = new EventEmitter<any>(); // Output event emitter to notify when the button is clicked

  /**
   * Handles the button click event.
   * Emits the event to the parent component.
   * @param event - The click event triggered by the button
   */
  onClickButton(event: MouseEvent): void {
    this.onClick.emit(event); // Emit the click event to the parent component
  }
}
