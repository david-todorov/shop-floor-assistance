
import { Component, EventEmitter, HostListener, inject, OnInit, Output } from '@angular/core';
import { Router, RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSliderModule } from '@angular/material/slider';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { loginState } from '../../shared/types/login-state';
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
    RouterLink
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
 /**
   * Implementation of header component.
   * The header holds the title, menu, the information of the currently logged in user role and login/out button
   * @author Jossin Antony (contributor)
   */
export class HeaderComponent {

   /**
   * Declaration of variables
   */
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

  /**
   * Initialization: Get the logged in state information and set the UI button labels visibilty, icons
   * and other properties accordingly.
  */
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
        this.backendCommunicationService._loginUIState$.next(this.loginUIState);
        this.backendCommunicationService.setLoginStates(this.loginUIState);
      }
    });
  }

  //Helper function to set the UI labels and icons
  private setUIParameters() {
    this.btnLabel = this.loginUIState.buttonLabel;
    this.btnIcon = this.loginUIState.buttonIcon;
    this.btnVisible = this.loginUIState.isLoginVisible;
    this.showCurrentRole= this.loginUIState.isLoggedIn;
    this.userRole=this.loginUIState.currentRole;
  }

  /**
   * Button submit action. If the user is already logged in, log him out. 
   * Otherwise, redirect to the login page
  */
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

  /**
   * Menu entries action depending on: the current role (operstor/editor)
  */
  getHomeRoute(): string {
    switch (this.backendCommunicationService.getloginUIState().currentRole) {
      case 'editor':
        return '/editor-homepage';
      case 'operator':
        return '/operator-homepage';
      default:
        return '/';
    }
  }
  
  getOrdersRoute(): string {
    switch (this.backendCommunicationService.getloginUIState().currentRole) {
      case 'editor':
        return '/editor/orders';
      case 'operator':
        return '/operator/orders';
      default:
        return '/';
    }
  }


}
