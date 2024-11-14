import { taskTO } from "./taskTO"; // Adjust the path as needed

export type workflowTO = {
  id?: number;
  name: string;
  description?: string;
  createdBy?: number;
  updatedBy?: number;
  createdAt?: Date;
  updatedAt?: Date;

  tasks: taskTO[];
 
};
