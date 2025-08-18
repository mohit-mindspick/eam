package com.eam.auth.repository;

import com.eam.auth.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByTenantId(Long tenantId);
    Optional<UserGroup> findByName(String name);
}