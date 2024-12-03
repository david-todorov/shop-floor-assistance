export type productTO = {
    id: number; // Unique identifier for the product
    productNumber: string; // Product number, typically used for backend matching
    name: string; // Name of the product
    type: string; // Type or category of the product
    description: string; // Detailed description of the product
    language: string; // Language associated with the product
    country: string; // Country associated with the product
    packageSize: string; // Size of the product package (e.g., "500g", "10x2")
    packageType: string; // Type of the product package (e.g., "Box", "Blister")

    // Audit fields for tracking creation and updates
    createdBy?: string; // Optional: Identifier for the user who created the product
    updatedBy?: string; // Optional: Identifier for the user who last updated the product
    createdAt?: Date; // Optional: Timestamp of when the product was created
    updatedAt?: Date; // Optional: Timestamp of the last update to the product
};
