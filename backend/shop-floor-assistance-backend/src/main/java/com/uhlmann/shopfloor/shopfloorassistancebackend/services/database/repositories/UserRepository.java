package com.uhlmann.shopfloor.shopfloorassistancebackend.services.database.repositories;

import com.uhlmann.shopfloor.shopfloorassistancebackend.services.database.objects.UserDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing User data in the database.
 *
 * This interface extends JpaRepository, providing CRUD operations
 * for UserDBO entities, which represent users in the database.
 * This interface will probably be used in some of our services
 */
@Repository
public interface UserRepository extends JpaRepository<UserDBO, Long> {

    Optional<UserDBO> findByUsername(String username);
}
