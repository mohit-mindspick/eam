package com.eam.auth.repository;

import com.eam.auth.model.UserRoleLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleLocationRepository extends JpaRepository<UserRoleLocation, Long> {

    List<UserRoleLocation> findByUserId(Long userId);

    List<UserRoleLocation> findByLocationId(Long locationId);

    List<UserRoleLocation> findByUserIdAndLocationId(Long userId, Long locationId);
}

