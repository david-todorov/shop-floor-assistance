import { taskTO } from "./taskTO"; // Importing task type to represent the tasks within a workflow

export type workflowTO = {
  id?: number; // Optional: Unique identifier for the workflow
  name: string; // Name of the workflow (required)
  description?: string; // Optional: Description providing details about the workflow

  // Audit fields for tracking creation and updates
  createdBy?: number; // Optional: Identifier for the user who created the workflow
  updatedBy?: number; // Optional: Identifier for the user who last updated the workflow
  createdAt?: Date; // Optional: Timestamp indicating when the workflow was created
  updatedAt?: Date; // Optional: Timestamp indicating when the workflow was last updated

  tasks: taskTO[]; // Array of tasks (defined by the `taskTO` type) associated with the workflow
};
