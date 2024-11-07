export type equipmentTO = {
    number: string;            // To match equipmentNumber from backend
    name: string;
    type?: string;             // Optional if not always used
    description: string;
    createdBy?: string;        // Optional
    updatedBy?: string;        // Optional
    createdAt?: Date;          // Optional
    updatedAt?: Date;          // Optional
    orders?: any[];            // Adjust this type if necessary
};