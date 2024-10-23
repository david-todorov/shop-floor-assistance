import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-editor-dashboard',
  standalone: true,
  imports: [RouterLink, RouterModule, CommonModule],
  templateUrl: './editor-dashboard.component.html',
  styleUrl: './editor-dashboard.component.css',
  providers:[RouterLink, RouterModule]
})
export class EditorDashboardComponent {
  constructor(private routerLink: RouterLink){}

}
