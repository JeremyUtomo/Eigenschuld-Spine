import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";
import {QuestionDTO} from "../DTO/questionDTO.model";
import { QuestionaryDTO } from '../DTO/questionaryDTO.model';

@Injectable({
    providedIn: 'root'
})
export class QuestionaryService {
    private apiUrl = environment.api_url + '/v1/questionary';

    constructor(private http: HttpClient) {}

    getQuestionary(progressId: string): Observable<QuestionaryDTO> {
        return this.http.get<QuestionaryDTO>(`${this.apiUrl}/${progressId}`);
    }

    saveQuestionary(progressId: string , questions: {questions: QuestionDTO[]}): Observable<void> {
        return this.http.post<void>(`${this.apiUrl}/${progressId}`, questions).pipe()
    }
}
