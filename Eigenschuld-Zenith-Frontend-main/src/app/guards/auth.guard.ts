import {
    ActivatedRouteSnapshot,
    CanActivateFn,
    Router,
    RouterStateSnapshot
} from "@angular/router";
import {inject, Injectable} from "@angular/core";
import {AuthService} from "../service/auth.service";
import {Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class AuthGuard {

    static authGuardFn: CanActivateFn = async (
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Promise<boolean> => {
        const router = inject(Router);
        const authService = inject(AuthService);

        const isLoggedIn = await authService.isLoggedIn();

        if (isLoggedIn) {
            return true;
        } else {
            router.navigate(['']);
            return false;
        }
    }
}
