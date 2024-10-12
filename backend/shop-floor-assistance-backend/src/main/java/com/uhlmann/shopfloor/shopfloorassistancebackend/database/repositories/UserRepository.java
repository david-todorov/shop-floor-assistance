package com.uhlmann.shopfloor.shopfloorassistancebackend.database.repositories;

import com.uhlmann.shopfloor.shopfloorassistancebackend.database.objects.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, Long> {

}
