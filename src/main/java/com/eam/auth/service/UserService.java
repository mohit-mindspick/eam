package com.eam.auth.service;

import com.eam.auth.model.User;
import com.eam.auth.model.Role;
import com.eam.auth.model.Permission;
import com.eam.auth.repository.PasswordResetTokenRepository;
import com.eam.auth.repository.UserRepository;
import com.eam.auth.repository.RoleRepository;
import com.eam.auth.repository.PermissionRepository;
import com.eam.auth.utils.JwtService;
import com.eam.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    private static final int RANDOM_PASSWORD_LENGTH = 12;

    // Mock OTP storage
    private final java.util.Map<String, String> otpStore = new java.util.HashMap<>();

    // CRUD
    public User createUser(User user) {

        if(null == user.getUsername() || user.getUsername().isBlank())
        {
            if(null != user.getEmail() && !user.getEmail().isBlank()){
                user.setUsername(user.getEmail());
            } else if (null != user.getPhoneNumber() && !user.getPhoneNumber().isBlank()) {
                user.setUsername(user.getPhoneNumber());
            }
            else {
                throw new RuntimeException("Email or Phone number must be provided.");
            }
        }

        if(null == user.getPassword()){
            user.setPassword(passwordEncoder.encode(RandomStringGenerator.generateRandomString(RANDOM_PASSWORD_LENGTH)));
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean userPresentWithUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean userPresentWithEmail(String username) {
        return userRepository.findByEmail(username).isPresent();
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        existing.setPhoneNumber(updatedUser.getPhoneNumber());
        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Search by phone
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Search by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    //Search by Username
    public User findByUsername(String username) {
        User user =  userRepository.findByUsername(username).get();
        if(null == user)
            user = userRepository.findByEmail(username).get();
        if(null == user)
            user = userRepository.findByPhoneNumber(username).get();
        return user;
    }

    // Role assignment
    public User assignRoleToUser(Long userId, Long roleId) {
        User user = getUserById(userId);
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    // Permission assignment
    public User assignPermissionToUser(Long userId, Long permissionId) {
        User user = getUserById(userId);
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));
        user.getDirectPermissions().add(permission);
        return userRepository.save(user);
    }

    public User assignPermissionsToUser(Long userId, List<Long> permissionIds) {
        User user = getUserById(userId);
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        user.getDirectPermissions().addAll(permissions);
        return userRepository.save(user);
    }

    // Register
    public User register(User user) {
        user.setEnabled(true);
        return userRepository.save(user);
    }

    // Login
    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && password.equals(user.get().getPassword())) {
            return jwtService.generateJwtToken(username);
        }
        throw new RuntimeException("Invalid credentials");
    }

    // OTP Flow
    public String sendOtp(String phoneNumber) {
        userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("User not found"));
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStore.put(phoneNumber, otp);
        // In real-world, send via SMS gateway
        return "OTP sent: " + otp;
    }

    public String verifyOtp(String phoneNumber, String otp) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (otpStore.containsKey(phoneNumber) && otpStore.get(phoneNumber).equals(otp) && user.isPresent()) {
            otpStore.remove(phoneNumber);
            return jwtService.generateJwtToken(user.get().getUsername());
        }
        throw new RuntimeException("Invalid OTP");
    }
}