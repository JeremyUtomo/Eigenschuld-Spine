import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";
import {UserResponse} from "../models/user-response";
import {ExerciseChartDTO} from "../models/exercise-chart-dto";

@Injectable({
  providedIn: 'root',
})
export class ChartService {
  chartSense: {labels: string[], values: number[]} = {labels: [], values: []}
  chartFeel: {labels: string[], values: number[]} = {labels: [], values: []}

  constructor(private http: HttpClient,
  ) { }

  public getUserExerciseData(UserId: string): Observable<UserResponse> {
    return this.http.get<UserResponse>(environment.api_url+"/v1/user/"+ UserId).pipe();
  }

  public getChartIds(progressId: String): Observable<ExerciseChartDTO> {
    return this.http.get<ExerciseChartDTO>(environment.api_url+"/v1/chart/"+progressId).pipe()
  }

  public saveChartWithId(chartId: string, labels: string[], values: number[], colors: string[] ) {
    let chartDataBody = []
  
    for(let i = 0; i < labels.length; i++) {
      chartDataBody.push({name: labels[i], percentage: values[i], color: colors[i]})
    }
  
    this.http.post(environment.api_url+"/v1/chart/save/"+chartId, chartDataBody).pipe().subscribe((response: Object) => {})
  }  

  public parseChartData(chartDataArray: {name: string, percentage: string, color: string}[]): {labels: string[], values: number[], colors: string[]} {
    let chartData: {labels: string[], values: number[], colors: string[] } = {labels: [], values: [], colors: []}
  
    for(let i = 0; i < chartDataArray.length; i++) {
      let dataEntry = chartDataArray[i]
      chartData.labels.push(dataEntry.name);
      chartData.values.push(+dataEntry.percentage)
      chartData.colors.push(dataEntry.color)
    }
  
    return chartData;
  }  
}
