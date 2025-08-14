package com.eam.auth.controller;

import com.eam.auth.model.User;
import com.eam.auth.model.Role;
import com.eam.auth.model.Permission;
import com.eam.auth.model.UserRoleLocation;
import com.eam.auth.service.UserRoleLocationService;
import com.eam.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRoleLocationService userRoleLocationService;

    // ================== USER CRUD ==================
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ================== SEARCH BY PHONE ==================
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<User> getUserByPhoneNumber(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(userService.findByPhoneNumber(phoneNumber));
    }

    // ================== ROLE ASSIGNMENT ==================
    @PostMapping("/{id}/roles/{roleId}")
    public ResponseEntity<User> assignRoleToUser(@PathVariable Long id, @PathVariable Long roleId) {
        return ResponseEntity.ok(userService.assignRoleToUser(id, roleId));
    }

    // ================== PERMISSION ASSIGNMENT ==================
    @PostMapping("/{id}/permissions/{permissionId}")
    public ResponseEntity<User> assignPermissionToUser(@PathVariable Long id, @PathVariable Long permissionId) {
        return ResponseEntity.ok(userService.assignPermissionToUser(id, permissionId));
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<User> addPermissionsToRole(
            @PathVariable Long userId,
            @RequestBody List<Long> permissionIds
    ) {
        return ResponseEntity.ok(userService.assignPermissionsToUser(userId, permissionIds));
    }

    @PostMapping("/{userId}/location/{locationId}/role/{roleId}/assign")
    public ResponseEntity<UserRoleLocation> assignRoleToUserAtLocation(
            @PathVariable Long userId,
            @PathVariable Long locationId,
            @PathVariable Long roleId) {
        UserRoleLocation userRoleLocation = userRoleLocationService.assignRoleToUserAtLocation(userId, locationId, roleId);
        return ResponseEntity.ok(userRoleLocation);
    }
}