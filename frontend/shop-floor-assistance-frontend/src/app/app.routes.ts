import { Routes } from '@angular/router';
import { LoginComponent } from './login/login/login.component';
import { HomeComponent } from './home/home.component';

import { OperatorDashboardComponent } from './operator-dashboard/operator-dashboard.component';
import { EditorDashboardComponent } from './editor-dashboard/editor-dashboard.component';
import { HomeGuard } from './guards/home.guard';
import { EditorCreateWorkflowComponent } from './editor-create-workflow/editor-create-workflow.component';
import { OperatorAssistanceComponent } from './operator-assistance/operator-assistance.component';

export const routes: Routes = [
    {path:"login",component:LoginComponent},
    {path:"home",component:HomeComponent},
    {path:"", redirectTo:'/login', pathMatch:'full',},
    {path: "operator", component: OperatorDashboardComponent,},
    {path: "editor", component: EditorDashboardComponent, },
    {path:"create-workflow",component:EditorCreateWorkflowComponent},
    {path:"operator-guidance", component:OperatorAssistanceComponent
    },
    { path: "**", redirectTo: '/login' }
];
