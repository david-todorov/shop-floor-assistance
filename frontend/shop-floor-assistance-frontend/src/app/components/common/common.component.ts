import { Component } from '@angular/core';
import { MatCommonModule } from '@angular/material/core';

@Component({
  selector: 'app-common',
  standalone: true,
  imports: [MatCommonModule],
  templateUrl: './common.component.html',
  styleUrl: './common.component.css'
})
export class CommonComponent {

}
