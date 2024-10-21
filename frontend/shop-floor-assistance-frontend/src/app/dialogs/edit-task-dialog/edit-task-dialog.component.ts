import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { taskTO } from '../../types/taskTO';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';


@Component({
  selector: 'app-edit-task-dialog',
   standalone: true,
   imports: [ MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    FormsModule,
    CommonModule],
  templateUrl: './edit-task-dialog.component.html',
  styleUrl: './edit-task-dialog.component.css'
})
export class EditTaskDialogComponent {

    constructor(
    public dialogRef: MatDialogRef<EditTaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { task: taskTO, isEditMode: boolean }
  ) {}

  onSave(): void {
    this.dialogRef.close(this.data.task);
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
