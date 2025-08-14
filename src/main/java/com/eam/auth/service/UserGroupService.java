package com.eam.auth.service;

import com.eam.auth.model.User;
import com.eam.auth.model.UserGroup;
import com.eam.auth.repository.UserGroupRepository;
import com.eam.auth.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    public List<UserGroup> getAllUserGroups() {
        return userGroupRepository.findAll();
    }

    public UserGroup getUserGroupById(Long id) {
        return userGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserGroup not found with id " + id));
    }

    public List<UserGroup> getUserGroupsByClient(Long clientId) {
        return userGroupRepository.findByClientId(clientId);
    }

    public UserGroup createUserGroup(UserGroup userGroup) {
        return userGroupRepository.save(userGroup);
    }

    public UserGroup updateUserGroup(Long id, UserGroup updatedGroup) {
        return userGroupRepository.findById(id).map(group -> {
            group.setName(updatedGroup.getName());
            group.setDescription(updatedGroup.getDescription());
            group.setActive(updatedGroup.isActive());
            group.setClient(updatedGroup.getClient());
            group.setUsers(updatedGroup.getUsers());
            return userGroupRepository.save(group);
        }).orElseThrow(() -> new RuntimeException("User group not found"));
    }

    public void deleteUserGroup(Long id) {
        userGroupRepository.deleteById(id);
    }

    @Transactional
    public UserGroup addUserToGroup(Long groupId, Long userId) {
        UserGroup group = getUserGroupById(groupId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        group.getUsers().add(user);
        user.getUserGroups().add(group);
        return userGroupRepository.save(group);
    }

    @Transactional
    public UserGroup removeUserFromGroup(Long groupId, Long userId) {
        UserGroup group = getUserGroupById(groupId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        group.getUsers().remove(user);
        user.getUserGroups().remove(group);
        return userGroupRepository.save(group);
    }
}