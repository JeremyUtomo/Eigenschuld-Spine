import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {Hulpverlener} from "../DTO/HulpverlenerDTO.model";
import {environment} from "../../environments/environment";


@Injectable({
    providedIn: 'root'
})
export class HulpverlenerService {
    constructor(private http: HttpClient) {}

    getHulpverleners(): Observable<Hulpverlener[]> {
        return this.http.get<Hulpverlener[]>(environment.api_url+`/v1/user/hulpverleners`)
            .pipe(
                map((resultData: Hulpverlener[]) => {
                    return resultData.map(item => ({ firstName: item.firstName, lastName: item.lastName }));
                })
            );
    }
}
