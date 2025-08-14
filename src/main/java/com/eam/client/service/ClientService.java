package com.eam.client.service;

import com.eam.client.dto.ClientRequestDTO;
import com.eam.client.dto.ClientResponseDTO;
import com.eam.client.model.Client;
import com.eam.client.model.Industry;
import com.eam.client.model.Subscription;
import com.eam.client.repository.ClientRepository;
import com.eam.client.repository.IndustryRepository;
import com.eam.client.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final IndustryRepository industryRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ClientResponseDTO createClient(ClientRequestDTO dto) {
        Industry industry = industryRepository.findById(dto.getIndustryId())
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        Subscription subscription = subscriptionRepository.findById(dto.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        Client parentClient = null;
        if (dto.getParentClientId() != null) {
            parentClient = clientRepository.findById(dto.getParentClientId())
                    .orElseThrow(() -> new RuntimeException("Parent Client not found"));
        }

        Client client = Client.builder()
                .clientName(dto.getClientName())
                .subscription(subscription)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .clientCode(dto.getClientCode())
                .adminEmail(dto.getAdminEmail())
                .licenses(dto.getLicenses())
                .adminPhoneNumber(dto.getAdminPhoneNumber())
                .country(dto.getCountry())
                .industry(industry)
                .featuresEnabled(dto.getFeaturesEnabled())
                .parentClient(parentClient)
                .build();

        return mapToResponse(clientRepository.save(client));
    }

    public List<ClientResponseDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<ClientResponseDTO> getClientById(Long id) {
        return Optional.ofNullable(clientRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Client not found")));
    }

    private ClientResponseDTO mapToResponse(Client client) {
        return ClientResponseDTO.builder()
                .id(client.getId())
                .clientName(client.getClientName())
                .subscriptionName(client.getSubscription().getName())
                .subscriptionCode(client.getSubscription().getSubscriptionCode())
                .startDate(client.getStartDate())
                .endDate(client.getEndDate())
                .clientCode(client.getClientCode())
                .adminEmail(client.getAdminEmail())
                .licenses(client.getLicenses())
                .adminPhoneNumber(client.getAdminPhoneNumber())
                .country(client.getCountry())
                .industryName(client.getIndustry().getName())
                .industryCode(client.getIndustry().getIndustryCode())
                .featuresEnabled(client.getFeaturesEnabled())
                .parentClientName(client.getParentClient() != null ? client.getParentClient().getClientName() : null)
                .parentClientCode(client.getParentClient() != null ? client.getParentClient().getClientCode() : null)
                .build();
    }

    public Client updateClient(Long id, ClientRequestDTO dto) {
        Industry industry = industryRepository.findById(dto.getIndustryId())
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        Subscription subscription = subscriptionRepository.findById(dto.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        Client tempParentClient = null;
        if (dto.getParentClientId() != null) {
            tempParentClient = clientRepository.findById(dto.getParentClientId())
                    .orElseThrow(() -> new RuntimeException("Parent Client not found"));
        }
        final Client parentClient = tempParentClient;

        return clientRepository.findById(id)
                .map(existing -> {
                    existing.setId(existing.getId());
                    existing.setIndustry(industry);
                    existing.setSubscription(subscription);
                    existing.setParentClient(parentClient);
                    existing.setClientName(existing.getClientName());
                    existing.setStartDate(existing.getStartDate());
                    existing.setEndDate(existing.getEndDate());
                    existing.setClientCode(existing.getClientCode());
                    existing.setAdminEmail(existing.getAdminEmail());
                    existing.setLicenses(existing.getLicenses());
                    existing.setAdminPhoneNumber(existing.getAdminPhoneNumber());
                    existing.setCountry(existing.getCountry());
                    existing.setFeaturesEnabled(existing.getFeaturesEnabled());
                    return clientRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}