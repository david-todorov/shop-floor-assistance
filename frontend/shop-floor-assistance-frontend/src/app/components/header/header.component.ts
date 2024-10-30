
import { Component, EventEmitter, HostListener, inject, OnInit, Output } from '@angular/core';
import { Router, RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSliderModule } from '@angular/material/slider';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { loginUIState } from '../../shared/component-elements/login-state';
import { Location } from '@angular/common';
import { BackendCommunicationService } from '../../services/backend-communication.service';

@Component({
  selector: 'app-header',
  standalone: true,
    imports: [
    MatSlideToggleModule, 
    MatSliderModule, 
    MatToolbarModule, 
    MatIconModule,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  public btnLabel!: loginUIState['buttonLabel'];
  public btnIcon!: loginUIState['buttonIcon'];
  public btnVisible!: loginUIState['isLoginVisible'];
  private loginUIState!: loginUIState;

  constructor(
    private backendCommunicationService: BackendCommunicationService, 
    private location: Location, 
    private router:Router) { 
  }
  
  private setUIParameters() {
    this.btnLabel = this.loginUIState.buttonLabel;
    this.btnIcon = this.loginUIState.buttonIcon;
    this.btnVisible = this.loginUIState.isLoginVisible;
  }

  // @HostListener('window:beforeunload', ['$event'])
  // unloadHandler(event: Event) {
  //   // Handle refresh logic here
  // }

  ngOnInit(): void {
      this.backendCommunicationService.loginUIState$.subscribe(
      (state) => {
        this.loginUIState = state;
        this.setUIParameters();
      },
      ()=>{
        this.loginUIState= this.backendCommunicationService.getloginUIState();
        this.setUIParameters();
      }
    );

    this.location.subscribe((event) => {
      console.log(event)
      if(event.url=='/login'){
        this.loginUIState.isLoginVisible= false;
        this.backendCommunicationService._loginUIState$.next(this.loginUIState);
        this.backendCommunicationService.setLoginStates(this.loginUIState);
      }
    });
  }

  onSubmit() {
    this.loginUIState.isLoginVisible= false;
    this.loginUIState.buttonLabel= 'Log In';
    this.loginUIState.buttonIcon= 'login';
    // this.btnVisible=  this.loginUIState.isLoginVisible;
    this.backendCommunicationService._loginUIState$.next(this.loginUIState);
    this.backendCommunicationService.setLoginStates(this.loginUIState);
    this.router.navigateByUrl('/login');
  }
}
