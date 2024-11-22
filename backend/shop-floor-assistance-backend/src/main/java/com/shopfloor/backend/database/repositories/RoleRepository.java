package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.Role;
import com.shopfloor.backend.database.objects.RoleDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for RoleDBO entities.
 * Provides methods for performing CRUD operations on role data.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleDBO, Long> {

    /**
     * Checks if a role entity exists with the given name.
     *
     * @param role the role to check
     * @return true if a role entity exists with the given name, false otherwise
     */
    boolean existsByName(Role role);

    /**
     * Finds a role entity by its name.
     *
     * @param role the name of the role to find
     * @return the found RoleDBO entity
     */
    RoleDBO findByName(Role role);
}
