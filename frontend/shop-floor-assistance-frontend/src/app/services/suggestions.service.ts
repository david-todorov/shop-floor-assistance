import { Injectable } from '@angular/core';
import { itemTO } from '../types/itemTO';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { Subject } from 'rxjs';
import { itemDropEvent } from '../types/itemDropEventType';

@Injectable({
  providedIn: 'root'
})
export class SuggestionsService {

  private dropItem = new Subject<itemDropEvent>();
  drop$ = this.dropItem.asObservable();

  triggerDrop(dropEvent: CdkDragDrop<itemTO[]>, index: number): void {
    this.dropItem.next({dropEvent, index});
  }
  
}
