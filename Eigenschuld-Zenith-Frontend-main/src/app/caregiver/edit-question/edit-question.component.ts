import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { NavbarComponent } from '../../utilities/navbar/navbar.component';
import { CommonModule, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { v4 as uuidv4 } from 'uuid';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { UserService } from '../../service/user.service';
import { QuestionaryDTO } from '../../DTO/questionaryDTO.model'
import { QuestionDTO } from '../../DTO/questionDTO.model';
import { ActivatedRoute, Router } from '@angular/router';
import {QuestionaryService} from "../../service/questionary.service";
import {AuthService} from "../../service/auth.service";
import {CaregiverNavbarComponent} from "../../utilities/caregiver-navbar/caregiver-navbar.component";

@Component({
  selector: 'app-edit-question',
  standalone: true,
    imports: [NavbarComponent, NgFor, CommonModule, FormsModule, HttpClientModule, CaregiverNavbarComponent],
  templateUrl: './edit-question.component.html',
  styleUrl: './edit-question.component.scss'
})
export class EditQuestionComponent implements OnInit {
  public questions: QuestionDTO[] = []

  public newQuestion: string = "";
  private progressId: string | undefined = "";

  constructor(
    private userService: UserService,
    private activeRoute: ActivatedRoute,
    private questionaryService: QuestionaryService,
    private router: Router
  ) { }

  ngOnInit(): void {
      let exerciseNumber = +(this.activeRoute.snapshot.paramMap.get("exerciseNumber") ?? 1);
      let clientId = this.activeRoute.snapshot.paramMap.get("clientId");

      if(clientId != null) {
        this.getQuestionsForUser(clientId, exerciseNumber);
      } else {
        this.router.navigate(["caregiver-overview"]);
      }
  }


  public addQuestion() {
    if(this.newQuestion.length > 0) {
      let question = new QuestionDTO(uuidv4(), this.newQuestion, this.newQuestion.length+1, [])
      this.questions.push(question)
      this.newQuestion = "";
      this.save()
    }
  }

  public moveQuestionUp(currentIndex: number) {
    let question = this.questions.at(currentIndex)

    if(question != undefined && currentIndex > 0) {
      this.questions.splice(currentIndex, 1)
      this.questions.splice(currentIndex-1, 0, question)
      this.save()
    }
  }

  public moveQuestionDown(currentIndex: number) {
    let question = this.questions.at(currentIndex)

    if(question != undefined && currentIndex < this.questions.length) {
      this.questions.splice(currentIndex, 1)
      this.questions.splice(currentIndex+1, 0, question)
      this.save()
    }
  }

  public removeQuestion(id: string) {
    this.questions = this.questions.filter(question => question.id !== id)
    this.save()
  }

  public save() {
    this.setOrderNumbers();
    let body = { questions: this.questions };
    if (this.progressId == undefined) {
        console.error(404, "progressId not found")
        return;
    }

    this.questionaryService.saveQuestionary(this.progressId, body).subscribe()
  }

  private getQuestionsForUser(clientId: string, exerciseNumber: number) {
    this.userService.getUserExerciseDataForUser(clientId).subscribe(user => {
      this.progressId = user.exerciseProgressIds.at(exerciseNumber)?.progressId
      this.fetchQuestions()
    })
  }

  private fetchQuestions() {
    if(this.progressId == undefined) {
      return;
    }

      this.questionaryService.getQuestionary(this.progressId).subscribe(data => {
          this.questions = data.questions;
      });
  }

  private setOrderNumbers() {
    for(let i = 0; i < this.questions.length; i++) {
      this.questions[i].orderNumber = i
    }
  }
}
