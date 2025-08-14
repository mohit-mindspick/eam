package com.eam.auth.service;

import com.eam.auth.model.Permission;
import com.eam.auth.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public List<Permission> createPermissions(List<Permission> permissions) {
        return permissionRepository.saveAll(permissions);
    }

    public Permission updatePermission(Long id, Permission updated) {
        Permission existing = getPermissionById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return permissionRepository.save(existing);
    }

    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }
}