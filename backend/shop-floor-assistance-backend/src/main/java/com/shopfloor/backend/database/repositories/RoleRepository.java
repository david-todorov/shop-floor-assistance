package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.RoleDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing Roles data in the database.
 *
 * This interface extends JpaRepository, providing CRUD operations
 * for RolesDBO entities, which represent roles in the database.
 * This interface will probably be used in some of our services
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleDBO, Long> {

}
