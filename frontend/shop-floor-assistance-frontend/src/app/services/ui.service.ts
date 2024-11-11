import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UIService {
  private itemIndices: { [workflowIndex: number]: { [taskIndex: number]: number } } = {};
  private checkStatuses: {[workflow: number]: { [task: number]: {[item: number]: boolean}}} = {};


  

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

  getItemIndices(): {[workflowIndex: number]: { [taskIndex: number]: number} } {
    return this.itemIndices;
  }

  setItemIndices(itemIndices: { [workflowIndex: number]: { [taskIndex: number]: number } }): void {
    this.itemIndices = itemIndices;
  }

  // -------------------


  setSelectedItemStatus(workflow: number, task: number, item: number, status: boolean): void {
    if (!this.checkStatuses[workflow]) {
      this.checkStatuses[workflow] = {};
    }
    this.checkStatuses[workflow][task][item] = status;
  }


  
  getSelectedItemStatus(workflow: number, task: number, item: number): boolean | null {
    if (this.checkStatuses[workflow] && this.checkStatuses[workflow][task] && this.checkStatuses[workflow][task][item]!== undefined) {
      return this.checkStatuses[workflow][task][item];
    }
    return null;
  }

  getItemStatuses(): {[workflow: number]: { [task: number]: {[item: number]: boolean}}} {
    return this.checkStatuses;
  }

  setItemStatuses(itemStatuses: {[workflow: number]: { [task: number]: {[item: number]: boolean}}}): void {
    this.checkStatuses = itemStatuses;
  }

  //   setItemStatuses(itemStatuses: { [workflow: number]: { [task: number]: { [item: number]: boolean } } }): void {
  //   for (const workflow in itemStatuses) {
  //     if (itemStatuses.hasOwnProperty(workflow)) {
  //       this.checkStatuses[workflow] = this.checkStatuses[workflow] || {};
  //       for (const task in itemStatuses[workflow]) {
  //         if (itemStatuses[workflow].hasOwnProperty(task)) {
  //           this.checkStatuses[workflow][task] = this.checkStatuses[workflow][task] || {};
  //           for (const item in itemStatuses[workflow][task]) {
  //             if (itemStatuses[workflow][task].hasOwnProperty(item)) {
  //               this.checkStatuses[workflow][task][item] = itemStatuses[workflow][task][item];
  //             }
  //           }
  //         }
  //       }
  //     }
  //   }
  // }
}
