import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../../utilities/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { LetterService } from '../../service/letter.service';
import { UserService } from '../../service/user.service';
import { ExerciseDTO } from '../../models/exercise-dto';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-caregiver-letter',
  standalone: true,
  imports: [NavbarComponent, FormsModule],
  templateUrl: './caregiver-letter.component.html',
  styleUrl: './caregiver-letter.component.scss'
})
export class CaregiverLetterComponent {
  public letter: string = ""
  public clientName: string = ""
  private progressId: string | undefined

  constructor(private letterService: LetterService, 
    private userService: UserService, 
    private activeRoute: ActivatedRoute) {}

  ngOnInit(): void {
    const clientId = this.activeRoute.snapshot.paramMap.get("clientId");

    if (clientId === null) {
      throw new Error("Client ID is not available");
    }

    this.getLetter(clientId)
  }

  private getLetter(clientId: string) {
    this.userService.getUserExerciseDataForUser(clientId).pipe().subscribe(response => {
      this.clientName = `${response.firstname} ${response.infix ? response.infix + ' ' : ''}${response.lastname}`.trim();
      
      let exercise: ExerciseDTO = response.exerciseProgressIds[response.exerciseProgressIds.length-1]
      this.progressId = exercise.progressId;

      this.letterService.getLetter(this.progressId).subscribe(response => {
        this.letter = response.replaceAll(/\n/g, '\n')
      })
    })
  }
}
