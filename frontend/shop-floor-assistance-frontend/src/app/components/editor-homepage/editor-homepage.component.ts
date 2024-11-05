import { Component } from '@angular/core';
import { CardComponent} from '../../shared/component-elements/card/card.component';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-editor-homepage',
  standalone: true,
  imports: [CardComponent, ButtonComponent],
  templateUrl: './editor-homepage.component.html',
  styleUrl: './editor-homepage.component.css'
})
export class EditorHomepageComponent {

  constructor(private router: Router) {}

  selectoption(index: number) {
  switch (index) {
    case 0:
      this.router.navigate(['/editor/:id']); // Example of navigating with a parameter
      break;
    case 1:
      this.router.navigate(['/editor-equipment']);
      break;
    case 2:
      this.router.navigate(['/editor-product']);
      break;
    default:
      console.error('Invalid option selected');
  }
}
}
