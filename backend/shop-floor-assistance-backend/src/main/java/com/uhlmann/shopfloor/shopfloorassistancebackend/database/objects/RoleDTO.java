package com.uhlmann.shopfloor.shopfloorassistancebackend.database.objects;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column()
    private Role name; // EDITOR, OPERATOR

    @ManyToMany(mappedBy = "roles")
    private Set<UserDTO> users = new HashSet<>();

    // Constructors, Getters, and Setters

    public RoleDTO() {
        this.users = new HashSet<>();
    }

    public RoleDTO(Role name) {
        this.name = name;
    }
}


