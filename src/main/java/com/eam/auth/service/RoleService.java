package com.eam.auth.service;

import com.eam.auth.model.Feature;
import com.eam.auth.model.Permission;
import com.eam.auth.model.Role;
import com.eam.auth.model.RoleType;
import com.eam.auth.repository.FeatureRepository;
import com.eam.auth.repository.PermissionRepository;
import com.eam.auth.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final FeatureRepository featureRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role createRole(Role role) {
        if(null == role.getRoleType())
            role.setRoleType(RoleType.STANDARD);
        return roleRepository.save(role);
    }

    public Role updateRole(Long id, Role updated) {
        Role existing = getRoleById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return roleRepository.save(existing);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public void addPermissionToRole(Long roleId, Long permissionId) {
        Role role = getRoleById(roleId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + permissionId));

        role.getDirectPermissions().add(permission);
        roleRepository.save(role);
    }

    public void addFeatureToRole(Long roleId, Long featureId) {
        Role role = getRoleById(roleId);
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new EntityNotFoundException("Feature not found with id: " + featureId));

        role.getFeatures().add(feature);
        roleRepository.save(role);
    }

    public Role addPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = getRoleById(roleId);
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.getDirectPermissions().addAll(permissions);
        return roleRepository.save(role);
    }

    public Role addFeaturesToRole(Long roleId, List<Long> featureIds) {
        Role role = getRoleById(roleId);
        List<Feature> features = featureRepository.findAllById(featureIds);
        role.getFeatures().addAll(features);
        return roleRepository.save(role);
    }

    public Set<Permission> getEffectivePermissions(Long roleId) {
        Role role = getRoleById(roleId);
        Set<Permission> effectivePermissions = new HashSet<>(role.getDirectPermissions());
        for (Feature feature : role.getFeatures()) {
            effectivePermissions.addAll(feature.getPermissions());
        }
        return effectivePermissions;
    }

    @SuppressWarnings("unused")
    public Set<Permission> getAllPermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Permission> allPermissions = new HashSet<>();

        // Add direct permissions
        if (role.getDirectPermissions() != null) {
            allPermissions.addAll(role.getDirectPermissions());
        }

        // Add feature-based permissions
        if (role.getFeatures() != null) {
            for (Feature feature : role.getFeatures()) {
                Feature f = featureRepository.findById(feature.getId())
                        .orElseThrow(() -> new RuntimeException("Feature not found"));
                if (f.getPermissions() != null) {
                    allPermissions.addAll(f.getPermissions());
                }
            }
        }
        return allPermissions;
    }
}
