import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { BackendCommunicationService } from '../services/backend-communication.service';

export const roleGuard: CanActivateFn = 
(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const router:Router= inject(Router);
  const backendCommunicationService:BackendCommunicationService= inject(BackendCommunicationService);
  const protectedEditorRoutes: String[]= [
                                      
                                      '/operator/orders',
                                      '/operator/orders/:id', 
                                      '/editor/orders', 
                                      '/editor/orders/:id', 
                                      '/editor-orders/order-form', 
                                      '/editor-orders/create', 
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
  const protectedOperatorRoutes: String[]= [
                                      
                                      '/operator/orders',
                                      '/operator/orders/:id', 
                                    ];


  const loggedInAs: String | null= backendCommunicationService.getloginUIState().currentRole;
  console.log('loged as:', loggedInAs)
  console.log('includes url:', state.url, protectedEditorRoutes.includes(state.url))
  console.log('loged as:', loggedInAs)

  switch(loggedInAs){
    case 'editor':
      console.log('in editor rolegaurd')
      return protectedEditorRoutes.some(routePattern => {
        const regex = new RegExp(`^${routePattern.replace(/:\w+/g, '\\w+')}$`);
        return regex.test(state.url);
      });
    case 'operator':
      return !protectedOperatorRoutes.includes(state.url)? router.navigate(['/']):true;
  }
  return false;
};

