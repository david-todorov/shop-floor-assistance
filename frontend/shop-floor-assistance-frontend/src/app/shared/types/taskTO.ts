import { workflowTO } from "./workflowTO"; 
import { itemTO } from "./itemTO";

export type taskTO = {
  id?: number;
  name: string;
  description?: string;

  createdBy?: number;
  updatedBy?: number;
  createdAt?: Date;
  updatedAt?: Date;

  items: itemTO[]; 
};
