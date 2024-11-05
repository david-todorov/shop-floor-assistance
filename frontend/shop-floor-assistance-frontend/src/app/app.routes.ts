import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { EditorComponent } from './components/editor/editor.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { OperatorComponent } from './components/operator/operator.component';
import { OperatorViewWorkflowComponent } from './components/operator-view-workflow/operator-view-workflow.component';
import { EditorEditWorkflowComponent } from './components/editor-edit-workflow/editor-edit-workflow.component';
import { EditorHomepageComponent } from './components/editor-homepage/editor-homepage.component';
import { EditorEquipmentComponent } from './components/editor-equipment/editor-equipment.component';
import { EditorProductComponent } from './components/editor-product/editor-product.component';

export const routes: Routes = [
    {path: '', component:WelcomeComponent},
    {path: 'login', component:LoginRegisterComponent},
    {path: 'editor', component:EditorComponent},
    {path: 'operator', component:OperatorComponent,},
    {path: 'operator/:id', component:OperatorViewWorkflowComponent,},
    {path: 'editor', component:EditorComponent,},
    {path: 'editor/:id', component:EditorEditWorkflowComponent,},
    {path: 'editor-homepage', component:EditorHomepageComponent,},
    {path: 'editor-equipment', component:EditorEquipmentComponent,},
    {path: 'editor-product', component:EditorProductComponent,},
];
