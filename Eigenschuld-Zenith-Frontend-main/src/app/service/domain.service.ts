import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Domain } from "../models/domain.model";
import {environment} from "../../environments/environment";
export { Domain }


@Injectable({
    providedIn: 'root'
})
export class DomainService {
    private apiUrl = environment.api_url + '/v1/domain';

    constructor(private http: HttpClient) {}

    getAllDomains(): Observable<Domain[]> {
        return this.http.get<Domain[]>(environment.api_url+`/v1/domain/all`);
    }

    getDomainById(id: string): Observable<Domain> {
        return this.http.get<Domain>(environment.api_url+`/v1/domain/${id}`);
    }

    addDomain(domain: Domain): Observable<Domain> {
        return this.http.post<Domain>(environment.api_url+"/v1/domain", domain);
    }

    updateDomain(id: string, domain: Domain): Observable<Domain> {
        return this.http.put<Domain>(environment.api_url+`/v1/domain/${id}`, domain);
    }

    deleteDomain(id: string): Observable<void> {
        return this.http.delete<void>(environment.api_url+`/v1/domain/${id}`);
    }
}
