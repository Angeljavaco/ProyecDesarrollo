import { HttpInterceptorFn } from '@angular/common/http';

import { STORAGE_KEYS } from '../constants/storage-keys';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem(STORAGE_KEYS.TOKEN);

  if (!token) {
    return next(req);
  }
  
  const authRequest = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });

  return next(authRequest);
};
