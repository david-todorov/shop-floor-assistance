import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BackendCommunicationService } from '../../services/backend-communication.service';
import { catchError, of } from 'rxjs';
import { ProductTableComponent } from '../../shared/component-elements/product-table/product-table.component';
import { productTO } from '../../types/productTO';
import { ButtonComponent } from '../../shared/component-elements/button/button.component';
import { ProductTabl} from '../../types/ProductTabl';


@Component({
  selector: 'app-editor-product',
  standalone: true,
  imports: [ProductTableComponent, ButtonComponent],
  templateUrl: './editor-product.component.html',
  styleUrl: './editor-product.component.css'
})

export class EditorProductComponent implements OnInit {
  
  product!: productTO;
  loadedProduct!: productTO[];
  editDisabled: boolean = false;
  createDisabled: boolean = false;
  editBtnLabel: string = 'Edit Product';
  createBtnLabel: string = 'Create Product';

  // newEquipmentData: any = {
  //   number:'',
  //   name: '',
  //   description: ''
  // };

  constructor(
    private router: Router,
    private backendCommunicationService: BackendCommunicationService
  ) { }

  ngOnInit(): void {
    this.loadProduct();
    console.log("in console")
  }

  loadProduct(): void {
    this.backendCommunicationService.getEditorProduct().pipe(
      catchError((err) => {
        console.error('Error fetching product:', err);
        return of(null);
      })
    ).subscribe({
      next: (response) => {
        if (response) {
          this.loadedProduct = response; // Assign API response to loadedEquipment
          console.log('Product loaded:', this.loadedProduct);
        }
      },
      error: (err) => {
        console.log('An error occurred:', err);
      },
      complete: () => {
        this.loadedProduct= ProductTabl;
        console.log('done', this.loadedProduct);
      }
    });
  }

  productSelected($event: any): void {
    this.product = $event;
  }

  resolveButtonClick($event: any, action: string): void {
   if ($event.type === 'click') {
      if (action === 'create') {
        // Directly navigate to the create equipment route without checking for equipment
        this.router.navigateByUrl('/editor-product/create');
      } else if (action === 'edit') {
        if (this.product == null || this.product === undefined) {
          alert('You must specify a product!');
        } else {
          console.log(this.product);
          this.router.navigateByUrl('/editor/product'),( this.product.number);
        }
      }

    }
    return;
  }

}