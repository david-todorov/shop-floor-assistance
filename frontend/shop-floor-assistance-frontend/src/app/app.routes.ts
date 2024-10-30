import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { EditorComponent } from './components/editor/editor.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { AppComponent } from './app.component';
import { OperatorComponent } from './components/operator/operator.component';

export const routes: Routes = [
    {path: '', component:WelcomeComponent},
    {path: 'login', component:LoginRegisterComponent},
    {path: 'editor', component:EditorComponent},
    {path: 'operator', component:OperatorComponent},
];
