import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {ResponseDTO} from "../models/response-dto";

@Injectable({
    providedIn: 'root'
})
export class ResponseService {
    private apiUrl = environment.api_url;

    constructor(private http: HttpClient) {
    }

    saveResponse(response: ResponseDTO, questionaryId: string): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/v1/question/response/save/${questionaryId}`, response);
    }

    updateResponse(responseId: string, questionaryId: string, response: ResponseDTO): Observable<any> {
        return this.http.put<any>(`${this.apiUrl}/v1/question/response/update/${responseId}/${questionaryId}`, response);
    }
}
