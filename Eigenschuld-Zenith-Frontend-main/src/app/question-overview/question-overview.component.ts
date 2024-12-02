import {Component, OnInit} from '@angular/core';
import {UserService} from "../service/user.service";
import {QuestionaryService} from "../service/questionary.service";
import {CommonModule} from "@angular/common";
import {NavbarComponent} from "../utilities/navbar/navbar.component";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import { QuestionDTO } from '../DTO/questionDTO.model';
import {ResponseService} from "../service/response.service";
import {FormsModule} from "@angular/forms";
import {UserResponse} from "../models/user-response";
import {RoleResponse} from "../DTO/RoleResponseDTO.model";
import {CaregiverNavbarComponent} from "../utilities/caregiver-navbar/caregiver-navbar.component";

@Component({
    selector: 'app-question-overview',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent, CaregiverNavbarComponent],
    templateUrl: './question-overview.component.html',
    styleUrls: ['./question-overview.component.scss']
})
export class QuestionOverviewComponent implements OnInit {
    public questions: QuestionDTO[] = [];
    public newResponses: string[] = [];
    public role: string = "";
    private progressId: string = "";

    constructor(
        private questionaryService: QuestionaryService,
        private userService: UserService,
        private router: Router,
        private activeRoute: ActivatedRoute,
        private authService: AuthService,
        private responseService: ResponseService
    ) {}

    ngOnInit(): void {
        this.initializeQuestions();
        this.getRole();
    }

    initializeQuestions(): void {
        const routeUserId = this.activeRoute.snapshot.paramMap.get("userId");
        const userId = routeUserId || this.authService.getUserId();

        if (userId) {
            const exerciseNumber = +(this.activeRoute.snapshot.paramMap.get("exerciseNumber") ?? 1);

            this.userService.getUser(userId).subscribe((user: UserResponse) => {
                const exercise = user.exerciseProgressIds.at(exerciseNumber);
                if (exercise) {
                    this.progressId = exercise.progressId;
                    this.fetchQuestions();
                } else {
                    console.error("Exercise not found");
                }
            });
        } else {
            console.error("User not logged in");
            this.router.navigate([""]);
        }
    }

    fetchQuestions() {
        if (this.progressId) {
            this.questionaryService.getQuestionary(this.progressId).subscribe(data => {
                this.questions = data.questions;
                this.newResponses = new Array(this.questions.length).fill('');
            });
        }
    }

    addResponse(questionId: string, index: number): void {
        const userId = this.authService.getUserId();
        const responseText = this.newResponses[index];

        if (userId && responseText.trim() !== "") {
            const response = {
                response: responseText,
                user: { id: userId },
                question: { id: questionId },
            };

            this.responseService.saveResponse(response, this.progressId).subscribe(() => {
                this.newResponses[index] = "";
                this.fetchQuestions();
            }, error => {
                console.error("Error saving response:", error);
            });
        }
    }

    scrollToQuestion(index: number): void {
        const element = document.getElementById(`question-${index}`);
        if (element) {
            element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }

    private getRole() {
        this.authService.getRole().subscribe((response: RoleResponse) => {
            this.role = response.role;
        });
    }
}
