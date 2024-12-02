import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LetterService {
  private apiUrl = environment.api_url + '/v1/letter/';

  constructor(private http: HttpClient) {}

  public getLetter(letterId: string): Observable<string> {
    const headers = new HttpHeaders().set('Content-Type', 'text/plain');
    return this.http.get<string>(`${this.apiUrl}${letterId}`, { headers }).pipe()
  }

  public saveLetter(letterId: string, letter: string): Observable<string> {
    const headers = new HttpHeaders().set('Content-Type', 'text/plain');
    let filterdLetter = letter.replaceAll(/\n/g, '\\n');
    filterdLetter = letter.replaceAll(/\"/g, '\\"')

    return this.http.post<string>(`${this.apiUrl}${letterId}`, `"${filterdLetter}"`, { headers }).pipe()
  }
}
