package com.uhlmann.shopfloor.shopfloorassistancebackend.services.security;

import com.uhlmann.shopfloor.shopfloorassistancebackend.services.database.objects.Role;
import com.uhlmann.shopfloor.shopfloorassistancebackend.services.database.objects.RoleDBO;
import com.uhlmann.shopfloor.shopfloorassistancebackend.services.database.objects.UserDBO;
import com.uhlmann.shopfloor.shopfloorassistancebackend.services.database.repositories.RoleRepository;
import com.uhlmann.shopfloor.shopfloorassistancebackend.services.database.repositories.UserRepository;
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
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void init() {

        //Creating two users without roles
        //The password is encoded with BCryptPasswordEncoder
        UserDBO operator = new UserDBO("operator", passwordEncoder.encode("operator"));
        UserDBO editor = new UserDBO("editor", passwordEncoder.encode("editor"));

        //Creating two roles
        RoleDBO operatorRole = new RoleDBO(Role.OPERATOR);
        RoleDBO editorRole = new RoleDBO(Role.EDITOR);

        //Associating users with roles
        operator.getRoles().add(operatorRole);
        editor.getRoles().add(editorRole);

        //Saving first the roles
        roleRepository.save(operatorRole);
        roleRepository.save(editorRole);

        //Saving secondly the users
        userRepository.save(operator);
        userRepository.save(editor);
    }
}
