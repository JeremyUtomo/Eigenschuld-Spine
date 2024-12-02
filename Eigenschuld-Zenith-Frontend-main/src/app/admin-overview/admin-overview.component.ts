import {Component, OnInit} from '@angular/core';
import {CommonModule} from "@angular/common";
import {Domain, DomainService} from "../service/domain.service";
import {HttpClientModule} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-overview',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './admin-overview.component.html',
  styleUrl: './admin-overview.component.scss'
})
export class AdminOverviewComponent implements OnInit{
    domains: Domain[] = [];

    constructor(private domainService: DomainService, private router: Router) { }

    ngOnInit(): void {
        this.loadDomains();
    }

    loadDomains(): void {
        this.domainService.getAllDomains().subscribe(domains => {
            this.domains = domains;
        });
    }

    addNewDomain(): void {
        this.router.navigate(['add-domain'])
    }
}
