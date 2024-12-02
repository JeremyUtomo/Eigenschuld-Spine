import { Component, OnInit } from '@angular/core';
import {InviteService} from "../../service/invite.service";
import {AuthService} from "../../service/auth.service";
import {FormsModule} from "@angular/forms";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {ClientProgressService} from "../../service/client-progress.service";
import {UserResponse} from "../../models/user-response";
import {NavbarComponent} from "../../utilities/navbar/navbar.component";
import {ExerciseDTO} from "../../models/exercise-dto";
import {Router} from "@angular/router";
import {resetParseTemplateAsSourceFileForTest} from "@angular/compiler-cli/src/ngtsc/typecheck/diagnostics";
import {CaregiverNavbarComponent} from "../../utilities/caregiver-navbar/caregiver-navbar.component";

@Component({
    selector: 'app-caregiver-overview',
    standalone: true,
    imports: [
        FormsModule,
        NgIf,
        NgForOf,
        NgClass,
        NavbarComponent,
        CaregiverNavbarComponent
    ],
    templateUrl: './caregiver-overview.component.html',
    styleUrl: './caregiver-overview.component.scss'
})
export class CaregiverOverviewComponent implements OnInit {
    public email: string = '';
    public errorMessage: string = '';
    public clients: UserResponse[] = [];
    private userId: string = this.authService.getUserId();


    constructor(
        private inviteService: InviteService,
        private authService: AuthService,
        private clientProgressService: ClientProgressService,
        private router: Router,
    ) {

    }

    ngOnInit(): void {
        this.getClients();
    }

    sendInvite(): void {
        this.email = this.email.trim();

        if (!this.email) {
            this.errorMessage = 'Email is leeg';
            return;
        }

        this.inviteService.sendInvite(this.userId, this.email).subscribe(
            response => {
                this.errorMessage = "Uitnodiging is gestuurd";
            },
            error => {
                this.errorMessage = "Een onverwachte fout is opgetreden";
            }
        );
    }

    getClients(): void {
        this.clientProgressService.getClients(this.userId).subscribe(
            (users: UserResponse[]) => {
                this.clients = users;
                this.reformatData(this.clients);
            }
        )
    }

    reformatData(clients: UserResponse[]) {
        for (let i = 0; i < this.clients.length; i++) {
            var date: Date = new Date(this.clients[i].lastlogin);
            this.clients[i].lastlogin = date.toLocaleDateString("en-UK").replace(/\//g, '-');


            for (let j = 0; j < 5; j++) {
                let str =  this.clients[i].exerciseProgressIds[j].name

                let matches = str.match(/(\d+)/);

                if (matches) {
                    this.clients[i].exerciseProgressIds[j].name = matches[0];
                }
            }
        }
    }

    openExercise(exercise: number, userId: string) {
        if(exercise != undefined && userId != undefined){
            if(exercise == 0 || exercise ==3) {
                this.router.navigate([`client/charts/${userId}/${exercise}`])
            } else if (exercise == 1 || exercise == 2) {
                this.router.navigate([`question-overview/${userId}/${exercise}`])
            } else if (exercise == 4) {
                this.router.navigate([`client/letter/${userId}`])
            }
        }
    }

    openEdit(exercise: number, userId: string) {
        this.router.navigate([`edit-question/${userId}/${exercise}`])
    }
}
