package com.eam.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TenantResponseDTO {
    private Long id;
    private String tenantName;
    private String subscriptionName;
    private String subscriptionCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private String tenantId;
    private String tenantCode;
    private String adminEmail;
    private Integer licenses;
    private String adminPhoneNumber;
    private String country;
    private String industryName;
    private String industryCode;
    private List<String> featuresEnabled;
    private String parentTenantName;
    private String parentTenantCode;
}
