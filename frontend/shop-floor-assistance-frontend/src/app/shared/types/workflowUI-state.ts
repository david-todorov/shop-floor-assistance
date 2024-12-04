 /**
   * Definition of a UIstate types to keep consistent behavior across different components.
   * @author Jossin Antony
*/
export type workflowStates= {
    [key: number]:{
        editMode: boolean, 
        showDescription: boolean,
        updatedTitle: string;
        updatedDescription?: string;
    }
}

export type itemUIStates= {
    [key: number]:{
        editMode: boolean, 
        showDescription: boolean,
        updatedTitle: string;
        updatedDescription?: string;
        upDatedTimeReq?: number | null;
    }
}

export type itemIndices= { [workflowIndex: number]: { [taskIndex: number]: number } };
export type itemCheckStatuses= {[workflow: number]: { [task: number]: {[item: number]: boolean}}};
export type taskCheckStatuses= {[workflow: number]: { [task: number]: boolean}};
