import {
    ActivatedRouteSnapshot,
    CanActivateFn,
    Router,
    RouterStateSnapshot
} from "@angular/router";
import {inject, Injectable} from "@angular/core";
import {AuthService} from "../service/auth.service";
import {Observable, of} from "rxjs";
import {map, catchError} from "rxjs/operators";
import {RoleResponse} from "../DTO/RoleResponseDTO.model";


@Injectable({providedIn: 'root'})
export class RoleGuard {

    static createGuard(requiredRole: string): CanActivateFn {
        return (
            route: ActivatedRouteSnapshot,
            state: RouterStateSnapshot
        ): Observable<boolean> => {
            const router = inject(Router);
            const authService = inject(AuthService);

            return authService.getRole().pipe(
                map((resultData: RoleResponse) => {
                    if (resultData.role === requiredRole) {
                        return true;
                    } else {
                        switch (resultData.role) {
                            case 'CLIENT': {
                                router.navigate(['home']);
                                break;
                            }
                            case 'HULPVERLENER': {
                                router.navigate(['caregiver-overview']);
                                break;
                            }
                            case 'ORGANISATIE': {
                                router.navigate(['zorginstatie']);
                                break;
                            }
                            case 'ADMIN': {
                                router.navigate(['admin-overview']);
                                break;
                            }
                        }
                        return false;
                    }
                }),
                catchError(() => {
                    router.navigate(['']);
                    return of(false);
                })
            );
        };
    }
}
