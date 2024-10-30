import { Component } from '@angular/core';
import { OrderTableComponent } from '../../shared/component-elements/order-table/order-table.component';

@Component({
  selector: 'app-operator',
  standalone: true,
  imports: [OrderTableComponent],
  templateUrl: './operator.component.html',
  styleUrl: './operator.component.css'
})
export class OperatorComponent {

}
