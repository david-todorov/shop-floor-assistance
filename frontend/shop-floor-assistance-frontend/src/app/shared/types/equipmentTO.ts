export type equipmentTO = {
    id?: number; // Optional: Unique identifier for the equipment
    equipmentNumber: string; // Equipment number, used to match with backend data
    name: string; // Name of the equipment
    type: string; // Type/category of the equipment
    description: string; // Description providing details about the equipment
    createdBy?: string; // Optional: Username or identifier of the creator
    updatedBy?: string; // Optional: Username or identifier of the last person who updated the equipment
    createdAt?: Date; // Optional: Timestamp of when the equipment was created
    updatedAt?: Date; // Optional: Timestamp of the last update made to the equipment
    orders?: []; // Optional: Array to hold associated orders, if applicable
};
