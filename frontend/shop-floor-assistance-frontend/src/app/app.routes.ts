import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { OperatorComponent } from './components/operator/operator.component';
import { OperatorViewWorkflowComponent } from './components/operator-view-workflow/operator-view-workflow.component';
import { EditorOrderComponent } from './components/editor-order/editor-order.component';
//import { EditorEditWorkflowComponent } from './components/editor-edit-workflow/editor-edit-workflow.component';
import { EditorHomepageComponent } from './components/editor-homepage/editor-homepage.component';
import { EditorEquipmentComponent } from './components/editor-equipment/editor-equipment.component';
import { EditorProductComponent } from './components/editor-product/editor-product.component';
import { EditorCreateEquipmentComponent } from './components/editor-create-equipment/editor-create-equipment.component';
import { EditorCreateProductComponent } from './components/editor-create-product/editor-create-product.component';
import { EditorEditEquipmentComponent } from './components/editor-edit-equipment/editor-edit-equipment.component';
import { EditorEditProductComponent } from './components/editor-edit-product/editor-edit-product.component';

export const routes: Routes = [
    { path: '', component: WelcomeComponent },
    { path: 'login', component: LoginRegisterComponent },
    { path: 'operator', component: OperatorComponent, },
    { path: 'operator/:id', component: OperatorViewWorkflowComponent, },
    { path: 'editor/orders', component: EditorOrderComponent, },
    //{path: 'editor/:id', component:EditorEditWorkflowComponent,},
    { path: 'editor-homepage', component: EditorHomepageComponent, },
    { path: 'editor/equipment', component: EditorEquipmentComponent, },
    { path: 'editor/equipment/:id', component: EditorEditEquipmentComponent, },
    { path: 'editor-equipment/create', component: EditorCreateEquipmentComponent, },
    { path: 'editor/products', component: EditorProductComponent, },
    { path: 'editor-product/create', component: EditorCreateProductComponent },
    { path: 'editor/product/:id', component: EditorEditProductComponent, },
];
