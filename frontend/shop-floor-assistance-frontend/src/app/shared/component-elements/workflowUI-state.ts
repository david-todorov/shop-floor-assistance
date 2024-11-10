export type workflowStates= {
    [key: number]:{
        editMode: boolean, 
        showDescription: boolean,
        updatedTitle: string;
        updatedDescription: string;
    }
}

export type itemUIStates= {
    [key: number]:{
        editMode: boolean, 
        showDescription: boolean,
        updatedTitle: string;
        updatedDescription: string;
        upDatedTimeReq: number | null;
    }
}



export type itemCheckedStatus= {
  checked: boolean;
}

export type taskCheckedStatus= {
  items: itemCheckedStatus[];
}

export type workflowCheckedStatus= {
  tasks: taskCheckedStatus[];
}
