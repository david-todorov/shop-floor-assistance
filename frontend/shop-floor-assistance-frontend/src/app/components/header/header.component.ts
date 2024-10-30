
import { Component, EventEmitter, HostListener, inject, OnInit, Output } from '@angular/core';
import { Router, RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSliderModule } from '@angular/material/slider';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { loginState } from '../../shared/component-elements/login-state';
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

  public btnLabel!: loginState['buttonLabel'];
  public btnIcon!: loginState['buttonIcon'];
  public btnVisible!: loginState['isLoginVisible'];
  private loginUIState!: loginState;
  public showCurrentRole= false;
  public userRole: string | null ='';

  constructor(
    private backendCommunicationService: BackendCommunicationService, 
    private location: Location, 
    private router:Router) { 
  }
  
  private setUIParameters() {
    this.btnLabel = this.loginUIState.buttonLabel;
    this.btnIcon = this.loginUIState.buttonIcon;
    this.btnVisible = this.loginUIState.isLoginVisible;
    this.showCurrentRole= this.loginUIState.isLoggedIn;
    this.userRole=this.loginUIState.currentRole;
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
      if(event.url=='/login'){
        this.loginUIState.isLoginVisible= false;
        console.log('in header browse back action',this.loginUIState);
        this.backendCommunicationService._loginUIState$.next(this.loginUIState);
        this.backendCommunicationService.setLoginStates(this.loginUIState);
      }
    });
  }

  onSubmit() {
    if(!this.loginUIState.isLoggedIn){


      this.loginUIState.isLoginVisible= false;
      this.loginUIState.buttonLabel= 'Log In';
      this.loginUIState.buttonIcon= 'login';
      this.backendCommunicationService._loginUIState$.next(this.loginUIState);
      this.backendCommunicationService.setLoginStates(this.loginUIState);
      this.router.navigateByUrl('/login');
    }
    else if(this.loginUIState.isLoggedIn){
      this.showCurrentRole= false;
      this.userRole='';

      this.loginUIState.isLoggedIn= false;
      this.loginUIState.buttonIcon= 'restart_alt';
      this.loginUIState.buttonLabel= 'Start';
      this.loginUIState.currentRole= null;
      this.loginUIState.isLoginVisible= true;
      this.loginUIState.jwtToken= '';
      this.loginUIState.rolesAvailable= [null];

      this.backendCommunicationService._loginUIState$.next(this.loginUIState);
      this.backendCommunicationService.setLoginStates(this.loginUIState);
      this.router.navigateByUrl('');
    }
  }
}
