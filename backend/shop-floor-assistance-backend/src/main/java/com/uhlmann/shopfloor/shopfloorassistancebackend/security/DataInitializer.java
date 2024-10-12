package com.uhlmann.shopfloor.shopfloorassistancebackend.security;

import com.uhlmann.shopfloor.shopfloorassistancebackend.database.objects.Role;
import com.uhlmann.shopfloor.shopfloorassistancebackend.database.objects.RoleDTO;
import com.uhlmann.shopfloor.shopfloorassistancebackend.database.objects.UserDTO;
import com.uhlmann.shopfloor.shopfloorassistancebackend.database.repositories.RoleRepository;
import com.uhlmann.shopfloor.shopfloorassistancebackend.database.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
        UserDTO operator = new UserDTO("operator", passwordEncoder.encode("operator"));
        UserDTO editor = new UserDTO("editor", passwordEncoder.encode("editor"));

        //Creating two roles
        RoleDTO operatorRole = new RoleDTO(Role.OPERATOR);
        RoleDTO editorRole = new RoleDTO(Role.EDITOR);

        //Associating users with roles and vice verse
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
