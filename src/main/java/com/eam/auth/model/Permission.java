package com.eam.auth.model;

import com.eam.util.AuditEntityListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditEntityListener.class)
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // e.g., "USER_CREATE"

    @Column(nullable = false)
    private String name;

    private String description;
}