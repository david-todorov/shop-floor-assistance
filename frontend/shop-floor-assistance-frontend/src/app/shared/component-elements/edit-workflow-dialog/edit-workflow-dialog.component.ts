import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { workflowTO } from '../../types/workflowTO';
import { ButtonComponent } from '../button/button.component';

@Component({
  selector: 'app-edit-workflow-dialog',
  standalone: true,
   imports: [ MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatDialogModule,
    MatButtonModule,
    EditWorkflowDialogComponent,
    ButtonComponent
  ],
  templateUrl: './edit-workflow-dialog.component.html',
  styleUrl: './edit-workflow-dialog.component.css'
})
export class EditWorkflowDialogComponent {

  form!: FormGroup;
  saveBtnLabel: string='Save';
  cancelBtnLabel: string='Cancel';
  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<EditWorkflowDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: workflowTO
  ) {
    this.form = this.fb.group({
      workflowname: [data.name],
      description: [data.description]
    });
  }

  onSave(): void {
    this.dialogRef.close(this.form.value);
  }

  onCancel(): void {
    this.dialogRef.close();
  }

}


