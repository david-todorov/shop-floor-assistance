import { Injectable } from '@angular/core';
import { itemTO } from '../types/itemTO';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { Subject } from 'rxjs';
import { itemDropEvent } from '../types/itemDropEventType';

@Injectable({
  providedIn: 'root'
})
export class SuggestionsService {
  
  private dropListIds: string[] = [];

  addDropListId(id: string): void {
    if (!this.dropListIds.includes(id)) {
      this.dropListIds.push(id);
    }
  }

  getDropListIds(): string[] {
    return this.dropListIds;
  }
  
}
