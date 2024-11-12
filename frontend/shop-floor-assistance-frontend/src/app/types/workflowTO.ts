export interface workflowTO {
    id?: number;
    name: string;
    description: string;
    order_id: number; // Single order ID associated with the workflow
    created_by?: number;
    updated_by?: number;
    created_at?: string; // ISO date string, adjust to Date if using Date objects
    updated_at?: string; // ISO date string, adjust to Date if using Date objects
}
