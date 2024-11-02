import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-editor-view-workflow',
  standalone: true,
  imports: [],
  templateUrl: './editor-view-workflow.component.html',
  styleUrl: './editor-view-workflow.component.css'
})
export class EditorViewWorkflowComponent {
  
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
