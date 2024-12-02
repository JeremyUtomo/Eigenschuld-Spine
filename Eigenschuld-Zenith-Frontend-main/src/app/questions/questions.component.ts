import { Component, OnInit } from '@angular/core';
import { CommonModule } from "@angular/common";
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from "@angular/router";
import { QuestionaryService } from "../service/questionary.service";
import { UserService } from "../service/user.service";
import { NavbarComponent } from "../utilities/navbar/navbar.component";
import { ResponseService } from "../service/response.service";
import { AuthService } from "../service/auth.service";
import { QuestionDTO } from '../DTO/questionDTO.model';
import { ResponseDTO } from "../models/response-dto";

@Component({
    selector: 'app-questions',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent],
    templateUrl: './questions.component.html',
    styleUrls: ['./questions.component.scss']
})
export class QuestionsComponent implements OnInit {
    public questions: QuestionDTO[] = [];
    public current_question = 0;
    public questions_length = 0;
    public currentResponse: string = "";
    private progressId: string = "";
    private exerciseNumber: number;

    constructor(
        private questionaryService: QuestionaryService,
        private userService: UserService,
        private router: Router,
        private activeRoute: ActivatedRoute,
        private authService: AuthService,
        private responseService: ResponseService
    ) {
        this.exerciseNumber = +(this.activeRoute.snapshot.paramMap.get("exerciseNumber") ?? 1);
    }

    ngOnInit(): void {
        this.loadUserExercise()
    }

    loadUserExercise(): void {
        const userId = this.authService.getUserId();

        if (userId) {
            this.userService.getUser(userId).subscribe((user: any) => {
                const exercise = user.exerciseProgressIds.at(this.exerciseNumber);
                if (exercise) {
                    this.progressId = exercise.progressId;
                    this.fetchQuestions();
                } else {
                    console.error("Exercise not found");
                }
            });
        } else {
            console.error("User not logged in");
            this.router.navigate(["/login"]);
        }
    }

    fetchQuestions() {
        if (this.progressId) {
            this.questionaryService.getQuestionary(this.progressId).subscribe(data => {
                this.questions = data.questions;
                this.questions_length = this.questions.length;
                this.loadResponseForCurrentQuestion();
            });
        }
    }

    loadResponseForCurrentQuestion() {
        if (this.questions[this.current_question].responses.length > 0) {
            this.currentResponse = this.questions[this.current_question].responses[0].response;
        } else {
            this.currentResponse = "";
        }
    }

    saveResponseForCurrentQuestion() {
        const userId = this.authService.getUserId();
        const currentResponse = this.currentResponse;
        const questionId = this.questions[this.current_question]?.id;
        let responseId = this.questions[this.current_question]?.responses[0]?.response_id;

        if (userId && currentResponse && questionId) {
            const response = new ResponseDTO(responseId, currentResponse, { id: userId }, { id: questionId });

            if (responseId) {
                this.responseService.updateResponse(responseId, this.progressId, response).subscribe(
                    () => {
                        this.fetchQuestions();
                    }
                );
            } else {
                this.responseService.saveResponse(response, this.progressId).subscribe(
                    () => {
                        this.fetchQuestions();
                    });
            }
        }
    }

    getProgressPercentage() {
        return ((this.current_question + 1) / this.questions_length) * 100;
    }

    next() {
        this.saveResponseForCurrentQuestion();
        if (this.current_question < this.questions.length - 1) {
            this.current_question++;
            this.loadResponseForCurrentQuestion();
        } else {
            this.router.navigate(["question-overview", this.exerciseNumber]);
        }
    }

    previous() {
        this.saveResponseForCurrentQuestion();
        if (this.current_question > 0) {
            this.current_question--;
            this.loadResponseForCurrentQuestion();
        } else {
            this.router.navigate(["home"]);
        }
    }
}
