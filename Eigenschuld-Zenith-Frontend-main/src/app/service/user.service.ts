import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {UserResponse} from "../models/user-response";
import {environment} from "../../environments/environment";
import { CookieService } from 'ngx-cookie-service';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private apiUrl = environment.api_url + '/v1/user';

    constructor(private http: HttpClient, private cookieService: CookieService) {}

    getUser(userId: string): Observable<UserResponse> {
        return this.http.get<UserResponse>(`${this.apiUrl}/${userId}`);
    }

    public getUserExerciseData(): Observable<UserResponse> {
      return this.http.get<UserResponse>(`${this.apiUrl}/${this.cookieService.get("userId")}`).pipe();
    }

    public getUserExerciseDataForUser(id: String): Observable<UserResponse> {
        return this.http.get<UserResponse>(`${this.apiUrl}/${id}`).pipe();
      }
}
