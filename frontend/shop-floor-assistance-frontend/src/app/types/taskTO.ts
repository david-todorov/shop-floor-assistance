import { workflowTO } from "./workflowTO"; 
import { itemTO } from "./itemTO";

export type taskTO = {
  id?: number;
  taskNumber: string;
  name: string;
  description?: string;
  workflows: workflowTO[];
  createdBy?: number;
  updatedBy?: number;
  createdAt?: Date;
  updatedAt?: Date;
  workflowBefore?: workflowTO;
  workflowAfter?: workflowTO;
  items?: itemTO[]; 
};
