import { workflowTO } from "./workflowTO";
import { productTO } from "./productTO"; 
import { equipmentTO } from "./equipmentTO"; 

export type orderTO = {
    id?: number; 
    orderNumber: string;
    name: string;
    description?: string; 

    workflows: workflowTO[]; 
    
    createdBy?: number;
    updatedBy?: number;
    createdAt?: Date;
    updatedAt?: Date;
    productBefore: productTO | null; 
    productAfter: productTO | null;
    equipment: equipmentTO[];
};
