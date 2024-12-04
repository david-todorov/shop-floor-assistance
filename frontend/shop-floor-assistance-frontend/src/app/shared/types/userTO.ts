 /**
   * Definition of the user transfer object, which is set during authentication.
   * @author Jossin Antony
*/
export type userTO={
    roles: [
        userRoleTO
    ],
    id: number,
    sub: "operator" | "editor",
    iat: number,
    exp: number
}

export enum userRoleTO {
    ROLE_OPERATOR= "ROLE_OPERATOR",
    ROLE_EDITOR= 'ROLE_EDITOR'
}
