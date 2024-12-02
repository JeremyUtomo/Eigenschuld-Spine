import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from './service/auth.service';
import {inject, Injectable} from "@angular/core";


export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const authToken = authService.getAuthToken();

    if (authToken) {
        req = req.clone({
            setHeaders: {
                Authorization: `Bearer ${authToken}`
            }
        });
    }

    return next(req);
};
