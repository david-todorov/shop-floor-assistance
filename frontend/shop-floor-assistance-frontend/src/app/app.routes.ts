import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { EditorComponent } from './components/editor/editor.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { AppComponent } from './app.component';
import { OperatorComponent } from './components/operator/operator.component';
import { EditorAccordionComponent } from './shared/component-elements/editor-accordion/editor-accordion.component';
import { OperatorViewWorkflowComponent } from './components/operator-view-workflow/operator-view-workflow.component';
import { EditorViewWorkflowComponent } from './components/editor-view-workflow/editor-view-workflow.component';

export const routes: Routes = [
    {path: '', component:WelcomeComponent},
    {path: 'login', component:LoginRegisterComponent},
    {path: 'editor', component:EditorComponent},
    {path: 'operator', component:OperatorComponent,},
    {path: 'operator/:id', component:OperatorViewWorkflowComponent,},
    {path: 'editor', component:EditorComponent,},
    {path: 'editor/:id', component:EditorViewWorkflowComponent,},
];
