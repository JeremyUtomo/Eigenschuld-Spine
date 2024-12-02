import { Component, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { NavbarComponent } from '../../utilities/navbar/navbar.component';
import Chart from 'chart.js/auto';
import { ChartService } from '../../service/chart.service';
import { Observable } from 'rxjs';
import { UserResponse } from '../../models/user-response';
import { ExerciseChartDTO } from '../../models/exercise-chart-dto';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-chart-overview',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './chart-overview.component.html',
  styleUrls: ['./chart-overview.component.scss']
})
export class ChartOverviewComponent {
  @ViewChild('feelingChart') feelingChartRef!: ElementRef;
  @ViewChild('senseChart') senseChartRef!: ElementRef;

  private feelingLabels: string[] = [];
  private feelingData: number[] = [];
  private feelingColors: string[] = [];
  private senseLabels: string[] = [];
  private senseData: number[] = [];
  private senseColors: string[] = [];
  private exerciseNumber: number = 0;

  constructor(private chartService: ChartService, private activeRoute: ActivatedRoute, private authService: AuthService) {
    let exerciseId: Observable<UserResponse> = this.chartService.getUserExerciseData(this.authService.getUserId());

    exerciseId.subscribe(async (response: UserResponse) => {
      this.exerciseNumber = +(this.activeRoute.snapshot.paramMap.get("exerciseNumber") ?? 0);

      let exerciseProgress: Observable<ExerciseChartDTO> = this.chartService.getChartIds(response.exerciseProgressIds[this.exerciseNumber].progressId);

      exerciseProgress.subscribe((secondResponse: ExerciseChartDTO) => {
        let parsedFeel = this.chartService.parseChartData(secondResponse.chartFeeling.values);
        let parsedSense = this.chartService.parseChartData(secondResponse.chartSense.values);

        const colorMap: { [label: string]: string } = {};
        parsedFeel.labels.forEach((label, index) => {
          colorMap[label.toLowerCase()] = parsedFeel.colors[index];
        });

        const updatedSenseColors = parsedSense.labels.map((label, index) => {
          const lowerCaseLabel = label.toLowerCase();
          return colorMap[lowerCaseLabel] || parsedSense.colors[index];
        });

        parsedSense.colors = updatedSenseColors;

        this.feelingLabels = parsedFeel.labels;
        this.feelingData = parsedFeel.values;
        this.feelingColors = parsedFeel.colors;
        this.senseLabels = parsedSense.labels;
        this.senseData = parsedSense.values;
        this.senseColors = parsedSense.colors;

        this.createFeelingChart();
        this.createSenseChart();
      });
    })
  }

  private createFeelingChart() {
    const ctx = this.feelingChartRef.nativeElement.getContext('2d');
    new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: this.feelingLabels,
        datasets: [{
          label: 'Feelings',
          data: this.feelingData,
          backgroundColor: this.feelingColors,
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        cutout: '70%',
        plugins: {
          legend: {
            position: 'top',
          }
        }
      }
    });
  }

  private createSenseChart() {
    const ctx = this.senseChartRef.nativeElement.getContext('2d');
    new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: this.senseLabels,
        datasets: [{
          label: 'Senses',
          data: this.senseData,
          backgroundColor: this.senseColors,
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        cutout: '70%',
        plugins: {
          legend: {
            position: 'top',
          }
        }
      }
    });
  }
}
