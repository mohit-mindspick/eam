package com.eam.auth.controller;

import com.eam.auth.annotation.HasPermission;
import com.eam.auth.model.Permission;
import com.eam.auth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    @HasPermission("PERMISSION_CREATE")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        return ResponseEntity.ok(permissionService.createPermission(permission));
    }

    @PostMapping("/bulk")
    @HasPermission("PERMISSION_CREATE")
    public ResponseEntity<List<Permission>> createPermissions(@RequestBody List<Permission> permissions) {
        return ResponseEntity.ok(permissionService.createPermissions(permissions));
    }

    @GetMapping
    @HasPermission("PERMISSION_READ")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/{id}")
    @HasPermission("PERMISSION_READ")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @PutMapping("/{id}")
    @HasPermission("PERMISSION_UPDATE")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        return ResponseEntity.ok(permissionService.updatePermission(id, permission));
    }

    @DeleteMapping("/{id}")
    @HasPermission("PERMISSION_DELETE")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}