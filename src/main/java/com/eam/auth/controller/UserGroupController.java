package com.eam.auth.controller;

import com.eam.auth.model.UserGroup;
import com.eam.auth.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-groups")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService userGroupService;

    @GetMapping
    public List<UserGroup> getAllGroups() {
        return userGroupService.getAllUserGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroup> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(userGroupService.getUserGroupById(id));
    }

    @GetMapping("/tenant/{tenantId}")
    public List<UserGroup> getGroupsByTenant(@PathVariable Long tenantId) {
        return userGroupService.getUserGroupsByTenant(tenantId);
    }

    @PostMapping
    public UserGroup createGroup(@RequestBody UserGroup userGroup) {
        return userGroupService.createUserGroup(userGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGroup> updateGroup(@PathVariable Long id, @RequestBody UserGroup userGroup) {
        try {
            return ResponseEntity.ok(userGroupService.updateUserGroup(id, userGroup));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        userGroupService.deleteUserGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/add-user/{userId}")
    public ResponseEntity<UserGroup> addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(userGroupService.addUserToGroup(groupId, userId));
    }

    @PostMapping("/{groupId}/remove-user/{userId}")
    public ResponseEntity<UserGroup> removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(userGroupService.removeUserFromGroup(groupId, userId));
    }
}