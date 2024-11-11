import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UIService {
  private itemIndices: { [workflowIndex: number]: { [taskIndex: number]: number } } = {};

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
}
