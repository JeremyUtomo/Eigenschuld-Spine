import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { NavbarComponent } from '../../utilities/navbar/navbar.component';
import { Chart, ChartConfiguration } from 'chart.js/auto';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { ChartService } from '../../service/chart.service';
import { Observable } from 'rxjs';
import { UserResponse } from '../../models/user-response';
import { ExerciseChartDTO } from '../../models/exercise-chart-dto';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-chart-feeling',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, NavbarComponent],
  templateUrl: './chart-feeling.component.html',
  styleUrls: ['./chart-feeling.component.scss']
})
export class ChartFeelingComponent implements AfterViewInit {
  @ViewChild('chartFeeling') chartFeelingRef!: ElementRef;
  chartFeeling!: Chart<"doughnut", number[], unknown>;

  chartData: { labels: string[], datasets: { label: string, data: number[], backgroundColor: string[], borderColor: string[], borderWidth: number }[] } = {
    labels: this.chartService.chartFeel.labels,
    datasets: [{
      label: 'percentage',
      data: this.chartService.chartFeel.values,
      backgroundColor: [],
      borderColor: [],
      borderWidth: 0
    }]
  };

  newSliceLabel: string = '';
  chartId: string = '';
  exerciseNumber: number = 0;

  constructor(private http: HttpClient,
    private router: Router,
    private chartService: ChartService,
    private activeRoute: ActivatedRoute,
    private authService: AuthService) { }

  ngAfterViewInit() {
    this.exerciseNumber = +(this.activeRoute.snapshot.paramMap.get("exerciseNumber") ?? 0);

    this.chartFeeling = new Chart(this.chartFeelingRef.nativeElement, {
      type: 'doughnut',
      data: this.chartData,
      options: {
        cutout: '70%',
        maintainAspectRatio: false,
        responsive: true
      }
    });

    this.updateChart()
  }

  addSlice() {
    if (!this.newSliceLabel) {
        return;
    }
    
    const label = this.newSliceLabel;
    const value = 0;
    this.chartData.labels.push(label);
    this.chartData.datasets[0].data.push(value);
    this.chartData.datasets[0].backgroundColor.push(this.getRandomColor());
    this.chartData.datasets[0].borderColor.push('white');
    this.newSliceLabel = '';
    this.chartFeeling.update();
    this.checkPercentages();
}


  removeSlice(index: number) {
    this.chartData.labels.splice(index, 1);
    this.chartData.datasets[0].data.splice(index, 1);
    this.chartData.datasets[0].backgroundColor.splice(index, 1);
    this.chartData.datasets[0].borderColor.splice(index, 1);
    this.chartFeeling.update();
    this.checkPercentages();
  }

  updateSlice(index: number, operation: '+' | '-') {
    const sum = this.checkSumData()
    const currentValue = this.chartData.datasets[0].data[index];

    if (operation === '+' && sum + 5 <= 100) {
      this.chartData.datasets[0].data[index] = currentValue + 5;
    } else if (currentValue > 0 && operation === "-") {
      this.chartData.datasets[0].data[index] = currentValue - 5;
    }
    this.chartFeeling.update();
    this.checkPercentages();
  }

  checkPercentages() {
    const sum = this.checkSumData()

    if (sum < 100) {
      const remainingPercentage = 100 - sum;

      const remainingIndex = this.chartData.labels.indexOf('Overig percentage');
      if (remainingIndex !== -1) {
        this.chartData.datasets[0].data[remainingIndex] = remainingPercentage;
      } else {
        this.chartData.labels.push('Overig percentage');
        this.chartData.datasets[0].data.push(remainingPercentage);
        this.chartData.datasets[0].backgroundColor.push('#CCCCCC');
        this.chartData.datasets[0].borderColor.push('white');
      }
    } else if (sum >= 100 && this.chartData.labels.includes('Overig percentage')) {
      const remainingIndex = this.chartData.labels.indexOf('Overig percentage');
      this.chartData.labels.splice(remainingIndex, 1);
      this.chartData.datasets[0].data.splice(remainingIndex, 1);
      this.chartData.datasets[0].backgroundColor.splice(remainingIndex, 1);
      this.chartData.datasets[0].borderColor.splice(remainingIndex, 1);
    }
    this.chartFeeling.update();
  }


  getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  checkSumData() {
    const sum = this.chartData.labels.includes('Overig percentage') ?
      this.chartData.datasets[0].data.reduce((acc, curr, index) =>
        this.chartData.labels[index] !== 'Overig percentage' ? acc + curr : acc, 0) :
      this.chartData.datasets[0].data.reduce((acc, curr) => acc + curr, 0);

    return sum;
  }

  save() {
    const sum = this.checkSumData()

    if (sum == 100) {
      this.chartService.saveChartWithId(this.chartId, this.chartData.labels, this.chartData.datasets[0].data, this.chartData.datasets[0].backgroundColor)
      this.router.navigate(['grafiek-verstand/'+this.exerciseNumber])
    }
  }

  private updateChart() {
    let exerciseId: Observable<UserResponse> = this.chartService.getUserExerciseData(this.authService.getUserId());

    exerciseId.subscribe(async (response: UserResponse) => {

      let exerciseProgress: Observable<ExerciseChartDTO> = this.chartService.getChartIds(response.exerciseProgressIds[this.exerciseNumber].progressId);

      exerciseProgress.subscribe((secondResponse: ExerciseChartDTO) => {
        let parsedChartData = this.chartService.parseChartData(secondResponse.chartFeeling.values)

        this.chartData.labels = parsedChartData.labels
        this.chartData.datasets[0].data = parsedChartData.values
        this.chartData.datasets[0].borderWidth = 0;

        for (let i = 0; i < parsedChartData.values.length; i++) {
          this.chartData.datasets[0].backgroundColor.push(parsedChartData.colors[i]);
        }

        this.chartId = secondResponse.chartFeeling.id

        this.chartFeeling.update()
        this.checkPercentages()
      })
    })
  }
}

