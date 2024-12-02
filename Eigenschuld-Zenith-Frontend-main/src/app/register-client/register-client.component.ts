import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import {CookieService} from "ngx-cookie-service";
import {RegisterResponse} from "../DTO/RegisterResponseDTO.model";

@Component({
  selector: 'app-register-client',
  standalone: true,
    imports: [
        FormsModule,
        NgIf
    ],
  templateUrl: './register-client.component.html',
  styleUrl: './register-client.component.scss'
})
export class RegisterClientComponent {
    public form!: FormGroup;
    public email: string = '';
    public password: string = '';
    public firstName: string = '';
    public infix: string = '';
    public lastName: string = '';
    public errorMessage: string = '';
    private inviteToken: string = '';


    constructor(
        private formBuilder: FormBuilder,
        private http: HttpClient,
        private router: Router,
        private authService: AuthService,
        private cookieService: CookieService,
        private activeRoute: ActivatedRoute,

    ) {

    }
    ngOnInit() : void {
        this.form = this.formBuilder.group({
            email: '',
            password: '',
            firstName: '',
            infix: '',
            lastName: ''
        })

        this.activeRoute.params.subscribe(params => {
            this.inviteToken = params['token'];
        });
    }

    validateEmail(email: string): boolean {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    validatePassword(password: string): boolean {
        const re = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
        return re.test(password);
    }

    validateName(name: string): boolean {
        return name.trim().length > 0;
    }

    save() {
        if(this.errorCheck()) {
            return;
        }

        let bodyData = {
            "email" : this.email,
            "password" : this.password,
            "firstName": this.firstName,
            "infix": this.infix,
            "lastName": this.lastName,
            "invitetoken": this.inviteToken
        };

        this.authService.registerClient(bodyData).subscribe(
            (resultData: RegisterResponse) => {
                this.cookieService.set('token', resultData.token);
                this.cookieService.set('userId', resultData.userId);

                this.router.navigate([''])
            },
            (error: any) => this.handleErrorResponse(error)
        );
    }

    errorCheck() {
        if (!this.validateEmail(this.email)) {
            this.errorMessage = "Onjuiste email formaat";
            return true;
        }

        if (!this.validatePassword(this.password)) {
            this.errorMessage = "Wachtwoord heeft ten miste 8 characters nodig, letters en nummer gecombineerd";
            return true;
        }

        if (!this.validateName(this.firstName)) {
            this.errorMessage = "Voornaam nodig";
            return true;
        }

        if (!this.validateName(this.lastName)) {
            this.errorMessage = "Achternaam nodig";
            return true;
        }
        return false;
    }

    handleErrorResponse(error: any) {
        if (error.status === 409) {
            this.errorMessage = "Email word al gebruikt";
        } else if (error.status === 204) {
            this.errorMessage = "Wachtwoord is niet ingevuld";
        } else if (error.status === 404) {
            this.errorMessage = "Invite link is niet gevonden"
        } else if (error.status === 403) {
            this.errorMessage = "Invite link is niet meer geldig"
        } else {
            this.errorMessage = "Een onverwachte fout is opgetreden";
        }
    }
}
