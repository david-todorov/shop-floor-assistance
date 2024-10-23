export type userTO={
    roles: [
        "ROLE_OPERATOR" | 'ROLE_EDITOR'
    ],
    id: number,
    sub: "operator" | "editor",
    iat: number,
    exp: number
}
