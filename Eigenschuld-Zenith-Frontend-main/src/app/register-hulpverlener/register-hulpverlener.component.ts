import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {NgIf} from "@angular/common";
import {AuthService} from "../service/auth.service";
import {RegisterResponse} from "../DTO/RegisterResponseDTO.model";
import {CookieService} from "ngx-cookie-service";
import {map} from "rxjs/operators";
import {RoleResponse} from "../DTO/RoleResponseDTO.model";
import {LoginResponse} from "../DTO/LoginResponseDTO.model";


@Component({
  selector: 'app-register-hulpverlener',
  standalone: true,
    imports: [
        FormsModule,
        ReactiveFormsModule,
        NgIf
    ],
  templateUrl: './register-hulpverlener.component.html',
  styleUrl: './register-hulpverlener.component.scss'
})

export class RegisterHulpverlenerComponent {
    form!: FormGroup;
    email: string = '';
    password: string = '';
    firstName: string = '';
    infix: string = '';
    lastName: string = '';
    errorMessage: string = '';


    constructor(
        private formBuilder: FormBuilder,
        private http: HttpClient,
        private router: Router,
        private authService: AuthService,
        private cookieService: CookieService
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
            "lastName": this.lastName
        };

        this.authService.registerHulpverlener(bodyData).subscribe(
            (resultData: RegisterResponse) => {
                this.cookieService.set('token', resultData.token);
                this.cookieService.set('userId', resultData.userId);

                this.router.navigate(['caregiver-overview'])
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
        } else if (error.status === 406) {
            this.errorMessage = "Het domein is niet geregistreerd";
        } else if (error.status === 204) {
            this.errorMessage = "Wachtwoord is niet ingevuld";
        } else {
            this.errorMessage = "Een onverwachte fout is opgetreden";
        }
    }
}
