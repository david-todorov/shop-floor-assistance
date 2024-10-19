package com.shopfloor.backend.database.objects;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represent a role in our database
 * For now consists of name and list of users associated
 * It may not change
 * The relation between role and user is bidirectional manyToMany
 * The relation binding is declared in UserDBO, here it is just mapped
 * DBO stands for DatabaseObject
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column()
    private Role name; // EDITOR, OPERATOR

    @ManyToMany(mappedBy = "roles")
    private Set<UserDBO> users = new HashSet<>();

    // Constructors, Getters, and Setters

    public RoleDBO() {
        this.users = new HashSet<>();
    }

    public RoleDBO(Role name) {
        this.name = name;
    }
}


