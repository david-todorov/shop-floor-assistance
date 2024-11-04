export type workflowStates= {
    [key: number]:{
        editMode: boolean, 
        showDescription: boolean,
        updatedTitle: string;
        updatedDescription: string;
    }
}
