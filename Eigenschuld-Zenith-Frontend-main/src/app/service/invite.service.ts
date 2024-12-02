import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class InviteService {

    constructor(private http: HttpClient) { }

    sendInvite(userId: string, email: string): Observable<void> {
        const body = { email };
        return this.http.post<void>(`${environment.api_url}/v1/invite/send/${userId}`, body);
    }
}
