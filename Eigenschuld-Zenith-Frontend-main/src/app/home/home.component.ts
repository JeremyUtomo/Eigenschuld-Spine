import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../utilities/navbar/navbar.component';
import { Router } from '@angular/router';
import { ClientProgressService } from '../service/client-progress.service';
import { AuthService } from '../service/auth.service';
import {ExerciseDTO} from "../models/exercise-dto";

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [CommonModule, NavbarComponent],
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    public exercises: ExerciseDTO[] = [];
    public routes: string[] = [
        "grafiek-gevoel/0",
        "questions/1",
        "questions/2",
        "grafiek-gevoel/3",
        "letter"
    ];

    constructor(
        private router: Router,
        private clientProgressService: ClientProgressService,
        private authService: AuthService
    ) {}

    ngOnInit(): void {
        this.getClientProgress();
    }

    getClientProgress(): void {
        const userId = this.authService.getUserId();

        this.clientProgressService.getClientExerciseProgress(userId).subscribe(
            (progressData: ExerciseDTO[]) => {
                this.exercises = progressData;
                },

            error => {
                console.error('Error fetching client progress:', error);
            }
        );
    }

    navigateTo(stepRoute: string): void {
        this.router.navigate([stepRoute]);
    }
}
