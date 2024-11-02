import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { orderTO } from '../../types/orderTO';

@Component({
  selector: 'app-operator-view-workflow',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './operator-view-workflow.component.html',
  styleUrl: './operator-view-workflow.component.css'
})
export class OperatorViewWorkflowComponent {

    constructor(private route: ActivatedRoute) {
    this.route.params.subscribe(params => {
      console.log(params['id']); // access the id
    });
  }

  //or
  
  name: string= 'intial vlue';
  @Input() set id(value: string){
    this.name= value;
    console.log(this.name);
  }

}
