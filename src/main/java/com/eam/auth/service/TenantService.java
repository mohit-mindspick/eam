package com.eam.auth.service;

import com.eam.auth.dto.TenantRequestDTO;
import com.eam.auth.dto.TenantResponseDTO;
import com.eam.auth.model.Tenant;
import com.eam.auth.model.Industry;
import com.eam.auth.model.Subscription;
import com.eam.auth.repository.TenantRepository;
import com.eam.auth.repository.IndustryRepository;
import com.eam.auth.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final IndustryRepository industryRepository;
    private final SubscriptionRepository subscriptionRepository;

    public TenantResponseDTO createTenant(TenantRequestDTO dto) {
        Industry industry = industryRepository.findById(dto.getIndustryId())
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        Subscription subscription = subscriptionRepository.findById(dto.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        Tenant parentTenant = null;
        if (dto.getParentTenantId() != null) {
            parentTenant = tenantRepository.findById(dto.getParentTenantId())
                    .orElseThrow(() -> new RuntimeException("Parent Tenant not found"));
        }

        Tenant tenant = Tenant.builder()
                .tenantName(dto.getTenantName())
                .subscription(subscription)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .tenantCode(dto.getTenantCode())
                .adminEmail(dto.getAdminEmail())
                .licenses(dto.getLicenses())
                .adminPhoneNumber(dto.getAdminPhoneNumber())
                .country(dto.getCountry())
                .industry(industry)
                .featuresEnabled(dto.getFeaturesEnabled())
                .parentTenant(parentTenant)
                .build();

        return mapToResponse(tenantRepository.save(tenant));
    }

    public List<TenantResponseDTO> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<TenantResponseDTO> getTenantById(Long id) {
        return Optional.ofNullable(tenantRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Tenant not found")));
    }

    private TenantResponseDTO mapToResponse(Tenant tenant) {
        return TenantResponseDTO.builder()
                .id(tenant.getId())
                .tenantName(tenant.getTenantName())
                .subscriptionName(tenant.getSubscription().getName())
                .subscriptionCode(tenant.getSubscription().getSubscriptionCode())
                .startDate(tenant.getStartDate())
                .endDate(tenant.getEndDate())
                .tenantCode(tenant.getTenantCode())
                .adminEmail(tenant.getAdminEmail())
                .licenses(tenant.getLicenses())
                .adminPhoneNumber(tenant.getAdminPhoneNumber())
                .country(tenant.getCountry())
                .industryName(tenant.getIndustry().getName())
                .industryCode(tenant.getIndustry().getIndustryCode())
                .featuresEnabled(tenant.getFeaturesEnabled())
                .parentTenantName(tenant.getParentTenant() != null ? tenant.getParentTenant().getTenantName() : null)
                .parentTenantCode(tenant.getParentTenant() != null ? tenant.getParentTenant().getTenantCode() : null)
                .build();
    }

    public Tenant updateTenant(Long id, TenantRequestDTO dto) {
        Industry industry = industryRepository.findById(dto.getIndustryId())
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        Subscription subscription = subscriptionRepository.findById(dto.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        Tenant tempParentTenant = null;
        if (dto.getParentTenantId() != null) {
            tempParentTenant = tenantRepository.findById(dto.getParentTenantId())
                    .orElseThrow(() -> new RuntimeException("Parent Tenant not found"));
        }
        final Tenant parentTenant = tempParentTenant;

        return tenantRepository.findById(id)
                .map(existing -> {
                    existing.setId(existing.getId());
                    existing.setIndustry(industry);
                    existing.setSubscription(subscription);
                    existing.setParentTenant(parentTenant);
                    existing.setTenantName(existing.getTenantName());
                    existing.setStartDate(existing.getStartDate());
                    existing.setEndDate(existing.getEndDate());
                    existing.setTenantCode(existing.getTenantCode());
                    existing.setAdminEmail(existing.getAdminEmail());
                    existing.setLicenses(existing.getLicenses());
                    existing.setAdminPhoneNumber(existing.getAdminPhoneNumber());
                    existing.setCountry(existing.getCountry());
                    existing.setFeaturesEnabled(existing.getFeaturesEnabled());
                    return tenantRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
    }

    public void deleteTenant(Long id) {
        tenantRepository.deleteById(id);
    }
}
