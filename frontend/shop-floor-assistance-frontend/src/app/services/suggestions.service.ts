import { Injectable } from '@angular/core';
import { itemTO } from '../types/itemTO';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { Subject } from 'rxjs';
import { itemDropEvent } from '../types/itemDropEventType';

@Injectable({
  providedIn: 'root'
})
export class SuggestionsService {
  
  private dropItemIds: string[] = [];
  private dropTaskIds: string[] = [];


  addDropItemId(id: string): void {
    if (!this.dropItemIds.includes(id)) {
      this.dropItemIds.push(id);
    }
  }

  getDropItemIds(): string[] {
    return this.dropItemIds;
  }

  addDropTaskId(id: string): void {
    if (!this.dropTaskIds.includes(id)) {
      this.dropTaskIds.push(id);
    }
  }

  getDropTaskIds(): string[] {
    return this.dropTaskIds;
  }

  setDropTaskIdArray(tasklist: string[]){
    this.dropTaskIds= tasklist;
  }

  
}
