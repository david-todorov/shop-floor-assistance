import { Routes } from '@angular/router';
import { LoginComponent } from './login/login/login.component';
import { HomeComponent } from './home/home.component';
import { EditorCreateWorkflowComponent } from './editor-create-workflow/editor-create-workflow.component';

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
        path:"create-workflow",
        component: EditorCreateWorkflowComponent,
    }
];
