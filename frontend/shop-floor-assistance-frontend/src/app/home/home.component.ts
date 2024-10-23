import { Component, OnInit } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'] 
})
export class HomeComponent implements OnInit{

  constructor(private authService: AuthService, private router: Router,){}

  ngOnInit(): void {
    // if (this.authService.isOperator) {
    //       this.router.navigate(['/operator']);
    //     } else if (this.authService.isEditor) {
    //       this.router.navigate(['/editor']);
    //     } else {
    //       alert('Unauthorized role');
    //     }
      };
}
