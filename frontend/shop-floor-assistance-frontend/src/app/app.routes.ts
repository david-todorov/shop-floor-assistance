import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { EditorComponent } from './components/editor/editor.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';

export const routes: Routes = [
    {path: '', component:HomeComponent},
    {path: 'login', component:LoginRegisterComponent}
];
