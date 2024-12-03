import { workflowTO } from "./workflowTO"; // Importing workflow type for potential linkage (if needed in the future)
import { itemTO } from "./itemTO"; // Importing item type to represent the items in a task

export type taskTO = {
  id?: number; // Optional: Unique identifier for the task
  name: string; // Name of the task (required)
  description?: string; // Optional: Description providing details about the task

  // Audit fields for tracking creation and updates
  createdBy?: number; // Optional: Identifier for the user who created the task
  updatedBy?: number; // Optional: Identifier for the user who last updated the task
  createdAt?: Date; // Optional: Timestamp indicating when the task was created
  updatedAt?: Date; // Optional: Timestamp indicating when the task was last updated

  items: itemTO[]; // Array of items (defined by the `itemTO` type) associated with the task
};
