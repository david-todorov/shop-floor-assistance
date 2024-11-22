import { CdkDragDrop } from "@angular/cdk/drag-drop";
import { itemTO } from "./itemTO";

export type itemDropEvent= {
  dropEvent: CdkDragDrop<itemTO[]>,
  index: number,
}
