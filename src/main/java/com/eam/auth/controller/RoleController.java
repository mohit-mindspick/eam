package com.eam.auth.controller;

import com.eam.auth.model.Permission;
import com.eam.auth.model.Role;
import com.eam.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    // Mapping permissions directly to role
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> addPermissionToRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        roleService.addPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }

    // Mapping feature to role
    @PostMapping("/{roleId}/features/{featureId}")
    public ResponseEntity<Void> addFeatureToRole(@PathVariable Long roleId, @PathVariable Long featureId) {
        roleService.addFeatureToRole(roleId, featureId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<Role> addPermissionsToRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds
    ) {
        Role updatedRole = roleService.addPermissionsToRole(roleId, permissionIds);
        return ResponseEntity.ok(updatedRole);
    }

    // Add multiple features to a role
    @PostMapping("/{roleId}/features")
    public ResponseEntity<Role> addFeaturesToRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> featureIds
    ) {
        Role updatedRole = roleService.addFeaturesToRole(roleId, featureIds);
        return ResponseEntity.ok(updatedRole);
    }

    // Get all permissions (direct + from features)
    @GetMapping("/{roleId}/permissions")
    public Set<Permission> getRolePermissions(@PathVariable Long roleId) {
        return roleService.getEffectivePermissions(roleId);
    }
}