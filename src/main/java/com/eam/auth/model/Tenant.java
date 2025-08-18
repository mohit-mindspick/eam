package com.eam.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_code", nullable = false, length = 4, unique = true)
    private String tenantCode;

    @Column(nullable = false)
    private String tenantName;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private String adminEmail;

    private Integer licenses;

    private String adminPhoneNumber;

    private String country;

    private String logo;

    @ElementCollection
    @CollectionTable(name = "tenant_features", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "feature")
    private List<String> featuresEnabled;

    @ManyToOne
    @JoinColumn(name = "parent_tenant_id")
    private Tenant parentTenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id", nullable = false)
    private Industry industry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;
}
