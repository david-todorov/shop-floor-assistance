import { Injectable } from '@angular/core';
import { itemCheckStatuses, itemIndices, taskCheckStatuses } from '../shared/component-elements/workflowUI-state';

@Injectable({
  providedIn: 'root'
})
export class UIService {
  private itemIndices: itemIndices = {};
  private itemCheckStatuses: itemCheckStatuses = {};
  private taskCheckStatuses: taskCheckStatuses = {};


  

  setSelectedItemIndex(workflowIndex: number, taskIndex: number, itemIndex: number): void {
    if (!this.itemIndices[workflowIndex]) {
      this.itemIndices[workflowIndex] = {};
    }
    this.itemIndices[workflowIndex][taskIndex] = itemIndex;
  }

  getSelectedItemIndex(workflowIndex: number, taskIndex: number): number | null {
    if (this.itemIndices[workflowIndex] && this.itemIndices[workflowIndex][taskIndex] !== undefined) {
      return this.itemIndices[workflowIndex][taskIndex];
    }
    return null;
  }

  getItemIndices(): itemIndices {
    return this.itemIndices;
  }

  setItemIndices(itemIndices: itemIndices): void {
    this.itemIndices = itemIndices;
  }

  // -------------------


  setSelectedItemStatus(workflow: number, task: number, item: number, status: boolean): void {
    if (!this.itemCheckStatuses[workflow]) {
      this.itemCheckStatuses[workflow] = {};
    }
    this.itemCheckStatuses[workflow][task][item] = status;
  }


  
  getSelectedItemStatus(workflow: number, task: number, item: number): boolean | null {
    if (this.itemCheckStatuses[workflow] && this.itemCheckStatuses[workflow][task] && this.itemCheckStatuses[workflow][task][item]!== undefined) {
      return this.itemCheckStatuses[workflow][task][item];
    }
    return null;
  }

  getItemStatuses(): itemCheckStatuses {
    return this.itemCheckStatuses;
  }

  setItemStatuses(itemStatuses: itemCheckStatuses): void {
    this.itemCheckStatuses = itemStatuses;
  }

  // ---------------------------------------
  setTaskStatuses(taskStatuses: taskCheckStatuses): void {
    this.taskCheckStatuses = taskStatuses;
  }

  getTaskStatuses(): taskCheckStatuses {
    return this.taskCheckStatuses;
  }

}
