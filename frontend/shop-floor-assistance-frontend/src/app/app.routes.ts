import { Routes } from '@angular/router';
import { LoginComponent } from './login/login/login.component';
import { HomeComponent } from './home/home.component';
import { OperatorAssistanceComponent } from './operator-assistance/operator-assistance.component';

export const routes: Routes = [
    {
        path:"login",
        component:LoginComponent
    },
    {
        path:"",
        component:LoginComponent
    },
    {
        path:"home",
        component:HomeComponent
    },
    {
        path:"operator-guidance",
        component:OperatorAssistanceComponent
    },
];
