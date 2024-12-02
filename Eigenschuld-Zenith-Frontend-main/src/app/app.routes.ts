import { CanActivateFn, Routes} from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ChartFeelingComponent } from './chart/chart-feeling/chart-feeling.component';
import { ChartSenseComponent } from './chart/chart-sense/chart-sense.component';
import { ChartOverviewComponent } from './chart/chart-overview/chart-overview.component';
import {ZorginstantieComponent} from "./zorginstantie/zorginstantie.component";
import {AdminOverviewComponent} from "./admin-overview/admin-overview.component";
import {AddDomainComponent} from "./admin-overview/add-domain/add-domain.component";
import {QuestionsComponent} from "./questions/questions.component";
import {QuestionOverviewComponent} from "./question-overview/question-overview.component";
import { EditQuestionComponent } from './caregiver/edit-question/edit-question.component';
import {RegisterHulpverlenerComponent} from "./register-hulpverlener/register-hulpverlener.component";
import { ClientChartComponent } from './caregiver/client-chart/client-chart.component';
import {AuthGuard} from "./guards/auth.guard";
import {RoleGuard} from "./guards/role.guard";
import { LetterComponent } from './letter/letter.component';
import {CaregiverOverviewComponent} from "./caregiver/caregiver-overview/caregiver-overview.component";
import {RegisterClientComponent} from "./register-client/register-client.component";
import {HomeComponent} from "./home/home.component";
import { CaregiverLetterComponent } from './caregiver/caregiver-letter/caregiver-letter.component';


export const ZorginstantieGuard:
    CanActivateFn = RoleGuard.createGuard('ORGANISATIE');
export const HulpVerlenerGuard:
    CanActivateFn = RoleGuard.createGuard('HULPVERLENER');
export const AdminGuard:
    CanActivateFn = RoleGuard.createGuard('ADMIN');
export const ClientGuard:
    CanActivateFn = RoleGuard.createGuard('CLIENT');

export const routes: Routes = [
    {
    path: '',
    title: 'Login',
    component: LoginComponent
},
{
    path: "register-hulpverlener",
    title: "register - hulpverlener",
    component: RegisterHulpverlenerComponent
},
{
    path: "register/:token"   ,
    title: "register - client",
    component: RegisterClientComponent
},
{
  path: '',
  canActivate: [AuthGuard.authGuardFn],
  children: [
      {
          path: "grafiek-gevoel/:exerciseNumber",
          title: "Grafiek - Gevoel",
          component: ChartFeelingComponent,
          canActivate: [ClientGuard]
      },
      {
          path: "grafiek-verstand/:exerciseNumber",
          title: "Grafiek - Verstand",
          component: ChartSenseComponent,
          canActivate: [ClientGuard]
      },
      {
          path: "grafiek-overzicht/:exerciseNumber",
          title: "Grafiek - Overzicht",
          component: ChartOverviewComponent,
          canActivate: [ClientGuard]
      },
      {
          path: "zorginstantie",
          title: "zorginstantie",
          component: ZorginstantieComponent,
          canActivate: [ZorginstantieGuard]
      },
      {
          path: "admin-overview",
          title: "Adminoverview",
          component: AdminOverviewComponent,
          canActivate: [AdminGuard]
      },
      {
          path: "add-domain",
          title: "add - domain",
          component: AddDomainComponent,
          canActivate: [AdminGuard]
      },
      {
          path: "edit-question/:clientId/:exerciseNumber",
          title: "Edit question",
          component: EditQuestionComponent,
          canActivate: [HulpVerlenerGuard]
      },
      {
          path: "question-overview/:userId/:exerciseNumber",
          title: "Question - overview",
          component: QuestionOverviewComponent,
          canActivate: [HulpVerlenerGuard]
      },
      {
          path: "question-overview/:exerciseNumber",
          title: "Question - overview",
          component: QuestionOverviewComponent,
          canActivate: [ClientGuard]
      },
      {
          path: "questions/:exerciseNumber",
          title: "Questions",
          component: QuestionsComponent,
          canActivate: [ClientGuard]

      },
      {
          path: "caregiver-overview",
          title: "Caregiver-overview",
          component: CaregiverOverviewComponent,
          canActivate: [HulpVerlenerGuard]
      },
      {
          path: "client/charts/:clientId/:exerciseNumber",
          title: "Charts - client",
          component: ClientChartComponent,
          canActivate: [HulpVerlenerGuard]
      },
      {
          path: "home",
          title: "Home",
          component: HomeComponent,
          canActivate: [ClientGuard]
      },
      {
          path: "letter",
          title: "Brief",
          component: LetterComponent,
          canActivate: [ClientGuard]
      },
      {
        path: "client/letter/:clientId",
        title: "Brief - cliënt",
        component: CaregiverLetterComponent,
        canActivate: [HulpVerlenerGuard]
      }
  ]
},
];
