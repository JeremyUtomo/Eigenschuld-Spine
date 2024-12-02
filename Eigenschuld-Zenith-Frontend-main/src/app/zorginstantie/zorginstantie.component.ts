import { Component } from '@angular/core';
import {NgForOf, NgOptimizedImage} from "@angular/common";
import {FormBuilder, FormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {map} from "rxjs";
import {HulpverlenerService} from "../service/hulpverlener.service";
import {Hulpverlener} from "../DTO/HulpverlenerDTO.model";



@Component({
  selector: 'app-zorginstantie',
  standalone: true,
    imports: [
        NgOptimizedImage,
        NgForOf,
        FormsModule
    ],
  templateUrl: './zorginstantie.component.html',
  styleUrl: './zorginstantie.component.scss'
})
export class ZorginstantieComponent {
    hulpverleners: Hulpverlener[] = [];

    constructor(
        private http: HttpClient,
        private router: Router,
        private hulpverlenerService: HulpverlenerService
    ) {

    }

    ngOnInit(): void{
        this.loadHulpverleners()
    }

    loadHulpverleners() {
        this.hulpverlenerService.getHulpverleners()
            .subscribe(
                (hulpverleners: any[]) => {
                    this.hulpverleners = hulpverleners;
                },
                (error) => {
                    console.error('Error fetching hulpverleners:', error);
                }
            );
    }
}
