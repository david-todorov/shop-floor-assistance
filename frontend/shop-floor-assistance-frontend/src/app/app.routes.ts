import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { OperatorComponent } from './components/operator/operator.component';
import { EditorOrderComponent } from './components/editor-order/editor-order.component';
import { EditorEditOrderComponent } from './components/editor-edit-order/editor-edit-order.component';
import { EditorHomepageComponent } from './components/editor-homepage/editor-homepage.component';
import { EditorEquipmentComponent } from './components/editor-equipment/editor-equipment.component';
import { EditorProductComponent } from './components/editor-product/editor-product.component';
import { EditorCreateEquipmentComponent } from './components/editor-create-equipment/editor-create-equipment.component';
import { EditorCreateProductComponent } from './components/editor-create-product/editor-create-product.component';
import { EditorEditEquipmentComponent } from './components/editor-edit-equipment/editor-edit-equipment.component';
import { EditorEditProductComponent } from './components/editor-edit-product/editor-edit-product.component';
import { OrderFormComponent } from './components/order-form/order-form.component';
import { EditorCreateOrderComponent } from './components/editor-create-order/editor-create-order.component';
import { CreateEquipmentFromExistingComponent } from './components/create-equipment-from-existing/create-equipment-from-existing.component';
import { EquipmentCreationOptionComponent } from './components/equipment-creation-option/equipment-creation-option.component';
import { ProductCreationOptionComponent } from './components/product-creation-option/product-creation-option.component';
import { CreateProductFromExistingComponent } from './components/create-product-from-existing/create-product-from-existing.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
        { path: '', component: WelcomeComponent },
        { path: 'login', component: LoginRegisterComponent,},
        { path: 'operator/orders', component: OperatorComponent, canActivate:[authGuard] },
        // { path: 'operator/orders/:id', component: OperatorViewWorkflowComponent, canActivate:[authGuard] },
        { path: 'editor/orders', component: EditorOrderComponent, canActivate:[authGuard] },
        { path: 'editor/orders/:id', component:EditorEditOrderComponent, canActivate:[authGuard] },
        { path: 'editor-orders/order-form', component: OrderFormComponent, canActivate:[authGuard] },
        { path: 'editor-orders/create', component: EditorCreateOrderComponent, canActivate:[authGuard] },
        { path: 'editor-homepage', component: EditorHomepageComponent, canActivate:[authGuard] },
        { path: 'editor/equipment', component: EditorEquipmentComponent, canActivate:[authGuard] },
        { path: 'editor/equipment/:id', component: EditorEditEquipmentComponent,canActivate:[authGuard] },
        { path: 'editor-equipment/creation-option', component: EquipmentCreationOptionComponent, canActivate:[authGuard] },
        { path: 'editor-equipment/create', component: EditorCreateEquipmentComponent, canActivate:[authGuard] },
        { path: 'editor-equipment/create-from-existing', component: CreateEquipmentFromExistingComponent, canActivate:[authGuard] },
        { path: 'editor/products', component: EditorProductComponent, canActivate:[authGuard] },
        { path: 'editor/product/:id', component: EditorEditProductComponent, canActivate:[authGuard] },
        { path: 'editor-product/creation-option', component: ProductCreationOptionComponent,canActivate:[authGuard] },
        { path: 'editor-product/create', component: EditorCreateProductComponent, canActivate:[authGuard] },
        { path: 'editor-product/create-from-existing', component: CreateProductFromExistingComponent, canActivate:[authGuard] }
    ]
