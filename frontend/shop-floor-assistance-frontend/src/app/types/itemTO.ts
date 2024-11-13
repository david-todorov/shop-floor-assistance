export type itemTO = {
  id?: number;
  itemNumber: string;
  name: string;
  description?: string;
  timeRequired?: number| null;  
  createdBy?: number;
  updatedBy?: number;
  createdAt?: Date;
  updatedAt?: Date;
  taskId?: number; // This is an optional foreign key reference to the related task, adjust if needed
};
