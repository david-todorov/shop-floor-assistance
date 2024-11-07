export type productTO = {
    number: string;            // To match equipmentNumber from backend
    name: string;
    type?: string;             // Optional if not always used
    description: string;
    language: string;
    country: string;
    package_size?:string;
    package_type?: string;
    createdBy?: string;        // Optional
    updatedBy?: string;        // Optional
    createdAt?: Date;          // Optional
    updatedAt?: Date;          // Optional
};