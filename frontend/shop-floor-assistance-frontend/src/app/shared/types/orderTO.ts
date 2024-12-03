import { workflowTO } from "./workflowTO"; // Importing workflow type
import { productTO } from "./productTO"; // Importing product type
import { equipmentTO } from "./equipmentTO"; // Importing equipment type

export type orderTO = {
    id?: number; // Optional: Unique identifier for the order
    orderNumber: string; // Unique number identifying the order
    name: string; // Name of the order
    description?: string; // Optional: Description or additional details about the order

    workflows: workflowTO[]; // Array of workflows associated with the order

    // Audit fields for tracking creation and updates
    createdBy?: number; // Optional: Identifier of the user who created the order
    updatedBy?: number; // Optional: Identifier of the user who last updated the order
    createdAt?: Date; // Optional: Timestamp indicating when the order was created
    updatedAt?: Date; // Optional: Timestamp indicating when the order was last updated

    productBefore: productTO; // Details of the product before processing
    productAfter: productTO; // Details of the product after processing
    equipment: equipmentTO[]; // Array of equipment associated with the order

    forecast?: { // Optional: Forecast data for the order
        totalTimeRequired: number | null; // Total time required for the order, nullable
    };
};
