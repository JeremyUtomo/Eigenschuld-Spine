import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../utilities/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { LetterService } from '../service/letter.service';
import { UserService } from '../service/user.service';
import { ExerciseDTO } from '../models/exercise-dto';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-letter',
  standalone: true,
  imports: [NavbarComponent, FormsModule],
  templateUrl: './letter.component.html',
  styleUrl: './letter.component.scss'
})
export class LetterComponent implements OnInit {
  public letter: string = ""

  private progressId: string | undefined

  constructor(private letterService: LetterService, private userService: UserService, private toastrService: ToastrService) {}

  ngOnInit(): void {
    this.userService.getUserExerciseData().pipe().subscribe(response => {
      let exercise: ExerciseDTO = response.exerciseProgressIds[response.exerciseProgressIds.length-1]
      this.progressId = exercise.progressId;

      this.letterService.getLetter(this.progressId).subscribe(response => {
        this.letter = response.replaceAll(/\n/g, '\n')
      })

    })
  }

  public save() {
    if(this.progressId) {
      this.letterService.saveLetter(this.progressId, this.letter).subscribe(response => {
        this.toastrService.success("Brief is opgeslagen")
      })
    }
  }
}
