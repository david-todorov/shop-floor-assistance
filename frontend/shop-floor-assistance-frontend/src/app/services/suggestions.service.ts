import { Injectable } from '@angular/core';
import { itemTO } from '../shared/types/itemTO';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { Subject } from 'rxjs';
import { itemDropEvent } from '../shared/types/itemDropEventType';

@Injectable({
  providedIn: 'root'
})
 /**
   * Suggestions service
   * 
   * This file contains the helper functions to implement the suggestion service, especially the 'drag-drop'
   * functionality with repsect to the task/tab component.
   * @author Jossin Antony
   */
export class SuggestionsService {
  
  private dropItemIds: string[] = [];
  private dropTaskIds: string[] = [];

  //Add the item to be dropped in the list
  addDropItemId(id: string): void {
    if (!this.dropItemIds.includes(id)) {
      this.dropItemIds.push(id);
    }
  }

  getDropItemIds(): string[] {
    return this.dropItemIds;
  }

  //Add the task to be dropped in the list
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
