export type loginState= {
    isLoginVisible: boolean,
    isLoggedIn: boolean,
    buttonLabel: 'Log In' | 'Log Out' | 'Start',
    buttonIcon: 'login' | 'logout' | 'restart_alt',
    rolesAvailable: ['ROLE_OPERATOR' | 'ROLE_EDITOR' | null],
    currentRole: 'operator' | 'editor' | null,
    jwtToken: ''
}
