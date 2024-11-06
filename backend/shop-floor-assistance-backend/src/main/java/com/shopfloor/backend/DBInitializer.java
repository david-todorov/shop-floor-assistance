package com.shopfloor.backend;

import com.shopfloor.backend.database.objects.Role;
import com.shopfloor.backend.database.objects.RoleDBO;
import com.shopfloor.backend.database.objects.UserDBO;
import com.shopfloor.backend.database.repositories.RoleRepository;
import com.shopfloor.backend.database.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class adds two test users into the system
 * Two roles are assigned to them
 * In general we can add as much as we want users or roles
 * A user must always have a role
 *                   IMPORTANT
 * If you add new user and new role for him
 * First create the users then the role
 * Then associate the user with the role
 * Then FIRST save the role in the database
 * Finally save the user, the steps are important
 * This comes from the JPA mappings that are used
 */
@Transactional
@Component
public class DBInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DBInitializer(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void init() {

        // Check if both roles already exist
        if (roleRepository.existsByName(Role.OPERATOR) && roleRepository.existsByName(Role.EDITOR)) {
            // If both roles exist, check if users already exist
            if (userRepository.existsByUsername("operator") || userRepository.existsByUsername("editor")) {
                return; // Exit if the users and roles already exist
            }
        } else {
            // Create roles if they don't exist
            RoleDBO operatorRole = new RoleDBO(Role.OPERATOR);
            RoleDBO editorRole = new RoleDBO(Role.EDITOR);

            roleRepository.save(operatorRole);
            roleRepository.save(editorRole);
        }

        // Check if users already exist
        if (!userRepository.existsByUsername("operator")) {
            // Creating the operator user
            UserDBO operator = new UserDBO("operator", passwordEncoder.encode("operator"));
            operator.getRoles().add(roleRepository.findByName(Role.OPERATOR)); // Assign role
            userRepository.save(operator);
        }

        if (!userRepository.existsByUsername("editor")) {
            // Creating the editor user
            UserDBO editor = new UserDBO("editor", passwordEncoder.encode("editor"));
            editor.getRoles().add(roleRepository.findByName(Role.EDITOR)); // Assign role
            editor.getRoles().add(roleRepository.findByName(Role.OPERATOR)); // Also assign operator role
            userRepository.save(editor);
        }
    }
}
