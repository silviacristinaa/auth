package com.github.silviacristinaa.auth.repositories;

import com.github.silviacristinaa.auth.entities.Role;
import com.github.silviacristinaa.auth.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
