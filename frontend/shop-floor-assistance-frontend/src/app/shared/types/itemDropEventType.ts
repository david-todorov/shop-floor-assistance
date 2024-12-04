import { CdkDragDrop } from "@angular/cdk/drag-drop";
import { itemTO } from "./itemTO";
 /**
   * Definition of an item drop event, to be used in drag-drop event of items
   * @author Jossin Antony
*/
export type itemDropEvent= {
  dropEvent: CdkDragDrop<itemTO[]>,
  index: number,
}
