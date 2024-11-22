package com.shopfloor.backend.database.objects;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a role in the database.
 * Contains details about the role, including its name and associated users.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleDBO {

    /**
     * Unique identifier for the role.
     * Auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * Name of the role.
     * Stored as an enumerated type.
     */
    @Enumerated(EnumType.STRING)
    @Column()
    private Role name; // EDITOR, OPERATOR

    /**
     * Set of users associated with the role.
     * Mapped by the roles attribute in the UserDBO entity.
     */
    @ManyToMany(mappedBy = "roles")
    private Set<UserDBO> users = new HashSet<>();

    /**
     * Constructs a RoleDBO with an empty set of users.
     */
    public RoleDBO() {
        this.users = new HashSet<>();
    }

    /**
     * Constructs a RoleDBO with the specified role name.
     * @param name the name of the role
     */
    public RoleDBO(Role name) {
        this.name = name;
    }
}
