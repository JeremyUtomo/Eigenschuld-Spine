import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";
import {UserResponse} from "../models/user-response";
import {ExerciseDTO} from "../models/exercise-dto";

@Injectable({
    providedIn: 'root'
})
export class ClientProgressService {

    constructor(private http: HttpClient) { }

    private apiUrl = environment.api_url + '/v1/clientprogress';

    getClients(userId: string):Observable<UserResponse[]> {
        return this.http.get<UserResponse[]>(`${this.apiUrl}/${userId}`)
    }

    getClientExerciseProgress(userId: string): Observable<ExerciseDTO[]> {
        return this.http.get<ExerciseDTO[]>(`${this.apiUrl}/client/${userId}`);
    }
}
