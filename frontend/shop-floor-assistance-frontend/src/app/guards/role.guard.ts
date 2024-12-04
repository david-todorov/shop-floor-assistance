import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { BackendCommunicationService } from '../services/backend-communication.service';
 /**
   * Role guard
   * 
   * This file implements a role guard which ensures that URLS can be accessed by users only if they are logged in
   * with the respective role, i.e, operator/editor required for access.
   * This is achieved by defining protected routes for accessible to each role.
   * @author Jossin Antony
*/
export const roleGuard: CanActivateFn = 
(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const router:Router= inject(Router);
  const backendCommunicationService:BackendCommunicationService= inject(BackendCommunicationService);
  const protectedEditorRoutes: String[]= [
                                      
                                      '/operator/orders',
                                      '/operator/orders/:id', 
                                      '/editor/orders', 
                                      '/editor/orders/:id', 
                                      '/editor-orders/creation-option', 
                                      '/editor-orders/create-from-existing',
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

   /**
   * HAndling of navigation based on current role and allowed routes.
*/
  switch(loggedInAs){
    case 'editor':
      return protectedEditorRoutes.some(routePattern => {
        const regex = new RegExp(`^${routePattern.replace(/:\w+/g, '\\w+')}$`);
        return regex.test(state.url);
      });
    case 'operator':
      return !protectedOperatorRoutes.includes(state.url)? router.navigate(['/']):true;
  }
  return false;
};
