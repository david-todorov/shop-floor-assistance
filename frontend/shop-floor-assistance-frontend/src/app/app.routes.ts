import { Routes } from '@angular/router';
import { LoginComponent } from './login/login/login.component';
import { HomeComponent } from './home/home.component';
import { OperatorDashboardComponent } from './operator-dashboard/operator-dashboard.component';
import { EditorDashboardComponent } from './editor-dashboard/editor-dashboard.component';
import { HomeGuard } from './guards/home.guard';

export const routes: Routes = [
    {path:"login",component:LoginComponent},
    {path:"", redirectTo:'/login', pathMatch:'full',},
    {path:"home", canActivate: [HomeGuard],
        children:[
            { path: "operator", component: OperatorDashboardComponent, },
            { path: "editor", component: EditorDashboardComponent, }
        ]
    },
    { path: "**", redirectTo: '/login' }
];
