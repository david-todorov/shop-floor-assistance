export type equipmentTO = {
    id?: number;
    equipmentNumber: string;            // To match equipmentNumber from backend
    name: string;
    type: string;             
    description: string;
    createdBy?: string;        
    updatedBy?: string;       
    createdAt?: Date;          
    updatedAt?: Date;          
    orders: [];            
};