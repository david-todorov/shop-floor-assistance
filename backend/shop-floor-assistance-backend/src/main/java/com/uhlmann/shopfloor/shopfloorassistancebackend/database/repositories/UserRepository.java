package com.uhlmann.shopfloor.shopfloorassistancebackend.database.repositories;

import com.uhlmann.shopfloor.shopfloorassistancebackend.database.objects.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, Long> {

    Optional<UserDTO> findByUsername(String username);
}
