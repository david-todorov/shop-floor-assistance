import { HttpInterceptorFn } from '@angular/common/http';
import { BackendCommunicationService } from '../services/backend-communication.service';
import { inject } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const backendCommunicationService= inject(BackendCommunicationService);

  const token= backendCommunicationService.getloginUIState().jwtToken;
  const clonedReq= req.clone({
    setHeaders:{Authorization: `Bearer ${token}`}
  });
  return next(clonedReq);
};
