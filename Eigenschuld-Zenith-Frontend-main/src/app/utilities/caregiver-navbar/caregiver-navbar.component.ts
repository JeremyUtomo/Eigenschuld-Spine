import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-caregiver-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './caregiver-navbar.component.html',
  styleUrl: './caregiver-navbar.component.scss'
})
export class CaregiverNavbarComponent {
    public menuOpen = false;
    constructor(private router: Router, private authService: AuthService) {}

    navigateToCaregiverOverview(): void {
        this.router.navigate(['caregiver-overview']);
    }

    logout(): void {
        this.authService.logout();
        this.router.navigate(['']);
    }

    toggleMenu() {
        this.menuOpen = !this.menuOpen;
    }
}
