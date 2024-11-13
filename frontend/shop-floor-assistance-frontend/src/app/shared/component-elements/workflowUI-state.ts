export type workflowStates= {
    [key: number]:{
        editMode: boolean, 
        showDescription: boolean,
        updatedTitle: string;
        updatedDescription?: string;
    }
}

export type itemFlowStates= {
    [key: number]:{
        editMode: boolean, 
        showDescription: boolean,
        updatedTitle: string;
        updatedDescription?: string;
        upDatedTimeReq?: number | null;
        checkStatus: boolean;
    }
}
