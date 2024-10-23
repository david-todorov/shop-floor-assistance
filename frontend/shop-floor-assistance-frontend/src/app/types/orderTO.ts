import { workflowTO } from "./workflowTO";

export type orderTO= {
    orderNumber: string;
    name: string;
    shortDescription: string;
    workflows: workflowTO[];
}
