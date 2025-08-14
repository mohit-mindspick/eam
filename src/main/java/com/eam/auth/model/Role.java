package com.eam.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // e.g., "ADMIN"

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, length = 20)
    @Builder.Default
    private RoleType roleType = RoleType.STANDARD;

    // Direct Permissions
    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> directPermissions;

    // Assigned Features
    @ManyToMany
    @JoinTable(
            name = "role_features",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Feature> features;

    // Mapping roles at locations (if needed)
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<UserRoleLocation> userRoleLocations;
}