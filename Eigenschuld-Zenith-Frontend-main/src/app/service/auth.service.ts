import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {RegisterResponse} from "../DTO/RegisterResponseDTO.model";
import {environment} from "../../environments/environment";
import {LoginResponse} from "../DTO/LoginResponseDTO.model";
import {RoleResponse} from "../DTO/RoleResponseDTO.model";
import {CookieService} from "ngx-cookie-service";
import {UserService} from "./user.service";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {resetParseTemplateAsSourceFileForTest} from "@angular/compiler-cli/src/ngtsc/typecheck/diagnostics";


@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(
        private http: HttpClient,
        private cookieService: CookieService,
        private userService: UserService
        ) {}

    registerHulpverlener(userData: any): Observable<RegisterResponse> {
        this.cookieService.deleteAll();
        return this.http.post<RegisterResponse>(environment.api_url+`/v1/auth/register/hulpverlener`, userData);
    }

    registerClient(userData: any): Observable<RegisterResponse> {
        this.cookieService.deleteAll();
        return this.http.post<RegisterResponse>(environment.api_url+`/v1/auth/register`, userData);
    }

    getAuthToken(): string | null {
        return this.cookieService.get("token");
    }

    getUserId() {
        return this.cookieService.get("userId");
    }

    login(email: string, password: string): Observable<LoginResponse> {
        this.cookieService.deleteAll();
        const bodyData = { email, password };
        return this.http.post<LoginResponse>(environment.api_url+'/v1/auth/login', bodyData);
    }

    getRole(): Observable<RoleResponse> {
        return this.http.get<RoleResponse>(environment.api_url+`/v1/user/getRole/`+ this.getUserId())
    }

    logout() {
        this.cookieService.deleteAll();
    }

    async isLoggedIn(): Promise<boolean> {
        const userId = this.getUserId();
        const token = this.getAuthToken();

        if (!userId || !token) {
            return false;
        }

        try {
            const role = await this.getRole().toPromise();
            return !!role;
        } catch (error) {
            this.logout();
            return false;
        }
    }

}
