package com.eam.client.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ClientResponseDTO {
    private Long id;
    private String clientName;
    private String subscriptionName;
    private String subscriptionCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private String clientId;
    private String clientCode;
    private String adminEmail;
    private Integer licenses;
    private String adminPhoneNumber;
    private String country;
    private String industryName;
    private String industryCode;
    private List<String> featuresEnabled;
    private String parentClientName;
    private String parentClientCode;
}