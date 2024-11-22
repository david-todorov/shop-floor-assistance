export type OperatorExecutionTO = {
    id: number;                // Unique execution ID
    started_at: string;        // Start timestamp
    finished_at?: string | null; // Finish timestamp (nullable)
    started_by: number;        // ID of the user who started
    finished_by?: number | null; // ID of the user who finished (nullable)
    order_id: number;          // Associated order ID
    aborted: boolean;          // Aborted status
};
