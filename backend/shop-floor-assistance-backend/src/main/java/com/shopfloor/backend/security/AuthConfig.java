package com.shopfloor.backend.security;

import com.shopfloor.backend.database.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration class for authentication-related beans.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Configuration
public class AuthConfig {
    private final UserRepository userRepository;

    /**
     * Constructor for AuthConfig.
     *
     * @param userRepository the UserRepository to be used for user data access
     */
    public AuthConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Bean for UserDetailsService.
     * Provides user details based on the username.
     *
     * @return a UserDetailsService implementation
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Bean for BCryptPasswordEncoder.
     * Provides a password encoder for encoding passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for AuthenticationManager.
     * Provides an authentication manager for managing authentication.
     *
     * @param config the AuthenticationConfiguration to be used
     * @return an AuthenticationManager instance
     * @throws Exception if an error occurs while getting the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean for AuthenticationProvider.
     * Provides an authentication provider for authentication.
     *
     * @return an AuthenticationProvider instance
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

}
