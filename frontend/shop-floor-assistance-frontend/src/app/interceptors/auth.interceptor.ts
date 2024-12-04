import { HttpInterceptorFn } from '@angular/common/http';
import { BackendCommunicationService } from '../services/backend-communication.service';
import { inject } from '@angular/core';

 /**
   * Authorization interceptor
   * 
   * This file implements an interceptor, which intercepts every API call following a successful authentication,
   * and injects the authentication token in the header. This is required for the server to approve the requests
   * from the client.
   * @author Jossin Antony
*/
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const backendCommunicationService= inject(BackendCommunicationService);

  const token= backendCommunicationService.getloginUIState().jwtToken;
  const clonedReq= req.clone({
    setHeaders:{Authorization: `Bearer ${token}`}
  });
  return next(clonedReq);
};
