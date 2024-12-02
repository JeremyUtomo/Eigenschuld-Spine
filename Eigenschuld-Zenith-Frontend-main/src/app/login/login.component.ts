import {Component, isStandalone, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {HttpClientModule} from "@angular/common/http";
import { Router } from '@angular/router';
import {AuthService} from "../service/auth.service";
import {NgIf} from "@angular/common";
import {LoginResponse} from "../DTO/LoginResponseDTO.model";
import {CookieService} from "ngx-cookie-service";
import {UserService} from "../service/user.service";
import {RoleResponse} from "../DTO/RoleResponseDTO.model";

@Component({
  selector: 'app-login',
  standalone: true,
    imports: [
        ReactiveFormsModule,
        HttpClientModule,
        FormsModule,
        NgIf,
    ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit{
  form!: FormGroup;
  email: string = '';
  password: string = '';
  errorMessage: string = '';


  constructor(
      private formBuilder: FormBuilder,
      private http: HttpClient,
      private router: Router,
      private authService: AuthService,
      private cookieService: CookieService
  ) {
  }

  ngOnInit(): void {
      this.checkIfLoggedIn()
    this.form = this.formBuilder.group( {
      email: '',
      password: ''
    })
  }


    save(): void {
        this.authService.login(this.email, this.password).subscribe(
            (resultData: LoginResponse) => {
                this.cookieService.set('token', resultData.token);
                this.cookieService.set('userId', resultData.userId.toString());

                this.authService.getRole().subscribe((response: RoleResponse) => {
                    if (response.role === 'HULPVERLENER') {
                        this.router.navigate(['caregiver-overview']);
                    } else {
                        this.router.navigate(['home']);
                    }
                });
            },
            error => {
                this.errorMessage = 'wrong credentials';
            }
        );
    }

    async checkIfLoggedIn(): Promise<void> {
        const isLoggedIn = await this.authService.isLoggedIn();
        if (isLoggedIn) {
            this.router.navigate(['home']);
        }
    }
}
