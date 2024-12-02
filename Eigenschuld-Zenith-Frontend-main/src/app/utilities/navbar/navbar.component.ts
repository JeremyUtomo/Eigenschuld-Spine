import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from "../../service/auth.service";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [ RouterLink, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
    public menuOpen = false;
    constructor(private router: Router, private authService: AuthService) {}

    navigateToHome(): void {
        this.router.navigate(['home']);
    }

    logout(): void {
        this.authService.logout();
        this.router.navigate(['']);
    }

    toggleMenu() {
        this.menuOpen = !this.menuOpen;
    }
}
