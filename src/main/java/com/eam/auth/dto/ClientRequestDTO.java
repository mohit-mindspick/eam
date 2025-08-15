package com.eam.auth.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ClientRequestDTO {
    private String clientName;
    private Long subscriptionId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String clientId;
    private String clientCode;
    private String adminEmail;
    private Integer licenses;
    private String adminPhoneNumber;
    private String country;
    private Long industryId;
    private List<String> featuresEnabled;
    private Long parentClientId;
    private String logo;
}
