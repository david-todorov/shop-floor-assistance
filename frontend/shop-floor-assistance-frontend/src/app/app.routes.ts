import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { EditorComponent } from './components/editor/editor.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { AppComponent } from './app.component';

export const routes: Routes = [
    {path: '', component:WelcomeComponent},
    {path: 'login', component:LoginRegisterComponent}
];
