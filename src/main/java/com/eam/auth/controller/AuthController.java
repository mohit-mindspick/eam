package com.eam.auth.controller;

import com.eam.auth.model.User;
import com.eam.auth.security.JwtUtil;
import com.eam.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  /*  private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> req) {
      *//*  String username = req.get("username");
        String password = req.get("password");
        User user = userService.authenticate(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<String> roles = userService.getRoleCodes(user.getId());
        List<String> directPermissions = userService.getDirectPermissionCodes(user.getId());
        Set<String> expandedPermissions = new HashSet<>(directPermissions);
        for (String role : roles) {
            expandedPermissions.addAll(userService.getPermissionsByRoleCode(role));
        }
        String token = jwtUtil.generateToken(username, roles, new ArrayList<>(expandedPermissions));
        Map<String, String> resp = new HashMap<>();
        resp.put("token", token);*//*

        return ResponseEntity.ok();
    }*/
}
