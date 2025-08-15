package com.eam.auth.security;

import com.eam.auth.service.RoleService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.*;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RoleService roleService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RoleService roleService) {
        this.jwtUtil = jwtUtil;
        this.roleService = roleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                if (jwtUtil.isTokenValid(token)) {
                    Claims claims = jwtUtil.extractAllClaims(token);
                    String username = claims.getSubject();
                    List<String> roles = claims.get("roles", List.class);
                    List<String> permissions = claims.get("permissions", List.class);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    if (permissions != null) {
                        permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
                    }
                    if (roles != null) {
                        // You need a service to map roles to permissions
                        for (String role : roles) {
                            Set<String> rolePermissions = roleService.getEffectivePermissionsByRoleCode(role); // Inject roleService
                            permissions.addAll(rolePermissions);
                        }
                    }
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
