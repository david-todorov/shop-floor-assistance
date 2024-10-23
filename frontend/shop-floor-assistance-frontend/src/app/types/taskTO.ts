import { itemTO } from "./itemTO";

export type taskTO={
    name: string;
    description: string;
    items: itemTO[];
}
