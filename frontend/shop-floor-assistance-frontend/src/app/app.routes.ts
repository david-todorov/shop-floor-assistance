import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { LoginRegisterComponent } from './components/login-register/login-register.component';
import { OperatorComponent } from './components/operator/operator.component';
import { OperatorViewWorkflowComponent } from './components/operator-view-workflow/operator-view-workflow.component';
import { EditorEditOrderComponent } from './components/editor-edit-order/editor-edit-order.component';
import { EditorHomepageComponent } from './components/editor-homepage/editor-homepage.component';
import { EditorEquipmentComponent } from './components/editor-equipment/editor-equipment.component';
import { EditorProductComponent } from './components/editor-product/editor-product.component';
import { EditorCreateEquipmentComponent } from './components/editor-create-equipment/editor-create-equipment.component';
import { EditorCreateProductComponent } from './components/editor-create-product/editor-create-product.component';
import { EditorEditEquipmentComponent } from './components/editor-edit-equipment/editor-edit-equipment.component';
import { EditorEditProductComponent } from './components/editor-edit-product/editor-edit-product.component';
import { EditorCreateOrderComponent } from './components/editor-create-order/editor-create-order.component';
import { CreateEquipmentFromExistingComponent } from './components/create-equipment-from-existing/create-equipment-from-existing.component';
import { EquipmentCreationOptionComponent } from './components/equipment-creation-option/equipment-creation-option.component';
import { ProductCreationOptionComponent } from './components/product-creation-option/product-creation-option.component';
import { CreateProductFromExistingComponent } from './components/create-product-from-existing/create-product-from-existing.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';
import { EditorOrderComponent } from './components/editor-order/editor-order.component';
import { OrderCreationOptionComponent } from './components/order-creation-option/order-creation-option.component';
import { CreateOrderFromExistingComponent } from './components/create-order-from-existing/create-order-from-existing.component';

export const routes: Routes = [
        { path: '', component: WelcomeComponent },
        { path: 'login', component: LoginRegisterComponent,},
        { path: 'operator/orders', component: OperatorComponent, canActivate:[authGuard, roleGuard] },
        { path: 'operator/orders/:id', component: OperatorViewWorkflowComponent, canActivate:[authGuard] },
        { path: 'editor/orders', component: EditorOrderComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor/orders/:id', component:EditorEditOrderComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor-orders/creation-option', component: OrderCreationOptionComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor-orders/create', component: EditorCreateOrderComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor-orders/create-from-existing', component: CreateOrderFromExistingComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor-homepage', component: EditorHomepageComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor/equipment', component: EditorEquipmentComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor/equipment/:id', component: EditorEditEquipmentComponent,canActivate:[authGuard, roleGuard] },
        { path: 'editor-equipment/creation-option', component: EquipmentCreationOptionComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor-equipment/create', component: EditorCreateEquipmentComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor-equipment/create-from-existing', component: CreateEquipmentFromExistingComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor/products', component: EditorProductComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor/product/:id', component: EditorEditProductComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor-product/creation-option', component: ProductCreationOptionComponent,canActivate:[authGuard, roleGuard] },
        { path: 'editor-product/create', component: EditorCreateProductComponent, canActivate:[authGuard, roleGuard] },
        { path: 'editor-product/create-from-existing', component: CreateProductFromExistingComponent, canActivate:[authGuard, roleGuard] }
    ]
