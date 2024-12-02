import { Component } from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {DomainService} from "../../service/domain.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-domain',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-domain.component.html',
  styleUrl: './add-domain.component.scss'
})
export class AddDomainComponent {

    constructor(private domainService: DomainService, private router: Router) { }

    onSubmit(form: NgForm) {
        if (form.invalid) {
            return;
        }

        this.domainService.addDomain(form.value).subscribe({
            next: (response) => {
                console.log('Domain added successfully', response);
                form.reset();
            },
            error: (error) => {
                console.error('Failed to add domain', error);
            }
        });

        this.router.navigate(['admin-overview'])
    }
}
