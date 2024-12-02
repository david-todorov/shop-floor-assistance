/*
* Definition of a login states
    isLoginVisible: Determines visibility of login button in header                        
    isLoggedIn: Is currently logged in or not?                         
    buttonLabel: Set Log in button label based on current state of login 
    buttonIcon: Set Log in button icon based on current state of log in 
    rolesAvailable: Roles available for the logged in user
    currentRole: Current role among the available roles for the logged in user. Eg: Editor can log in as a n operator as well
    jwtToken: The jwt token created during log in
*/
export type loginState= {
    isLoginVisible: boolean,                        
    isLoggedIn: boolean,                            
    buttonLabel: 'Log In' | 'Log Out' | 'Start',    
    buttonIcon: 'login' | 'logout' | 'restart_alt', 
    rolesAvailable: ['ROLE_OPERATOR' | 'ROLE_EDITOR' | null],
    currentRole: 'operator' | 'editor' | null,
    jwtToken: ''
}
