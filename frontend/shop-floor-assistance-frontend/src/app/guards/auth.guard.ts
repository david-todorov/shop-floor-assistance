import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { BackendCommunicationService } from '../services/backend-communication.service';

export const authGuard: CanActivateFn = 
  (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const router:Router= inject(Router);
  const backendCommunicationService:BackendCommunicationService= inject(BackendCommunicationService);
  const protectedRoutes: String[]= [
                                      
                                      '/operator/orders',
                                      '/operator/orders/:id', 
                                      '/editor/orders', 
                                      '/editor/orders/:id', 
                                      '/editor-orders/creation-option', 
                                      '/editor-orders/create',
                                      '/editor-orders/create-from-existing',  
                                      '/editor-homepage', 
                                      '/editor/equipment', 
                                      '/editor/equipment/:id',
                                      '/editor-equipment/creation-option',
                                      '/editor-equipment/create',
                                      '/editor-equipment/create-from-existing', 
                                      '/editor/products', 
                                      '/editor/product/:id',
                                      '/editor-product/creation-option', 
                                      '/editor-product/create',
                                      '/editor-product/create-from-existing', 
                                    ];
  const isLoggedIn: boolean= backendCommunicationService.getloginUIState().isLoggedIn;
  console.log('in authguard', 'url is ', state.url,protectedRoutes.includes(state.url) && !isLoggedIn
    ? router.navigate(['/']): true)
  return protectedRoutes.includes(state.url) && !isLoggedIn
    ? router.navigate(['/']): true;
};



