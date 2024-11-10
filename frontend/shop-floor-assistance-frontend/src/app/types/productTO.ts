export type productTO = {
    productNumber: string;            // To match equipmentNumber from backend
    name: string;
    type: string;             
    description: string;
    language: string;
    country: string;
    packageSize:string;
    packageType: string;
    createdBy?: string;        // Optional
    updatedBy?: string;        // Optional
    createdAt?: Date;          // Optional
    updatedAt?: Date;          // Optional
};