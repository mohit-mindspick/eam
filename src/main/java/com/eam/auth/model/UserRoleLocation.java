package com.eam.auth.model;

import com.eam.location.model.Location;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_role_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Link to Role
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // Link to Location
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    // Optionally, you can add extra fields like creation date, etc.
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    // You can add more fields if you want to capture more details about the user's role at a location.
}

