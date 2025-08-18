package com.eam.auth.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TenantRequestDTO {
    private String tenantName;
    private Long subscriptionId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String tenantId;
    private String tenantCode;
    private String adminEmail;
    private Integer licenses;
    private String adminPhoneNumber;
    private String country;
    private Long industryId;
    private List<String> featuresEnabled;
    private Long parentTenantId;
    private String logo;
}
