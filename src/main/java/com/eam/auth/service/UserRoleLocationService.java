package com.eam.auth.service;

import com.eam.auth.model.User;
import com.eam.auth.model.Role;
import com.eam.auth.model.UserRoleLocation;
import com.eam.auth.repository.UserRoleLocationRepository;
import com.eam.auth.repository.UserRepository;
import com.eam.auth.repository.RoleRepository;
import com.eam.auth.model.Location;
import com.eam.auth.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleLocationService {

    private final UserRoleLocationRepository userRoleLocationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LocationRepository locationRepository;

    public UserRoleLocation assignRoleToUserAtLocation(Long userId,Long locationId,Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        Location location = locationRepository.findById(locationId).orElseThrow(() -> new RuntimeException("Location not found"));

        UserRoleLocation userRoleLocation = UserRoleLocation.builder()
                .user(user)
                .role(role)
                .location(location)
                .build();

        return userRoleLocationRepository.save(userRoleLocation);
    }
}
