package com.uhlmann.shopfloor.shopfloorassistancebackend.database.repositories;

import com.uhlmann.shopfloor.shopfloorassistancebackend.database.objects.RoleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleDTO, Long> {

}