export type itemTO = {
  id?: number; // Optional: Unique identifier for the item
  name: string; // Name of the item (required)
  description?: string; // Optional: Additional details or description of the item
  timeRequired?: number | null; // Optional: Estimated time required for the item (in minutes or seconds), can be null if not applicable
  createdBy?: number; // Optional: Identifier of the user who created the item
  updatedBy?: number; // Optional: Identifier of the user who last updated the item
  createdAt?: Date; // Optional: Timestamp indicating when the item was created
  updatedAt?: Date; // Optional: Timestamp indicating when the item was last updated
};
