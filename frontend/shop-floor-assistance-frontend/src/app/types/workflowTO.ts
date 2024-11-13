import { taskTO } from "./taskTO"; // Adjust the path as needed

export type workflowTO = {
  id?: number;
  workflowNumber: string;
  name: string;
  description?: string;
  tasks: taskTO[];
  createdBy?: number;
  updatedBy?: number;
  createdAt?: Date;
  updatedAt?: Date;
  taskBefore?: taskTO;
  taskAfter?: taskTO;
};
