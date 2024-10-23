import { taskTO } from "./taskTO";

export type workflowTO={
    name: string;
    description: string;
    tasks: taskTO[];
}
