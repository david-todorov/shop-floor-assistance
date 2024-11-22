package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.UserDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for UserDBO entities.
 * Provides methods for performing CRUD operations on user data.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Repository
public interface UserRepository extends JpaRepository<UserDBO, Long> {

    /**
     * Finds a user entity by its username.
     *
     * @param username the username to search for
     * @return an Optional containing the found UserDBO, or empty if not found
     */
    Optional<UserDBO> findByUsername(String username);

    /**
     * Checks if a user entity exists with the given username.
     *
     * @param username the username to check
     * @return true if a user entity exists with the given username, false otherwise
     */
    boolean existsByUsername(String username);
}
