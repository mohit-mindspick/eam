package com.eam.auth.repository;

import com.eam.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
   public Optional<Role> findByCode(String code);
}