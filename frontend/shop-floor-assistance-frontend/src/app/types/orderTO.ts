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
    productBefore: productTO; 
    productAfter: productTO;
    equipment: equipmentTO[];
    total_time_required?: number;
};
