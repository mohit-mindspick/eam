package com.eam.client.service;

import com.eam.auth.model.Feature;
import com.eam.auth.repository.FeatureRepository;
import com.eam.client.model.Subscription;
import com.eam.client.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private FeatureRepository featureRepository;

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(Long id, Subscription updatedSubscription) {
        return subscriptionRepository.findById(id).map(subscription -> {
            subscription.setSubscriptionCode(updatedSubscription.getSubscriptionCode());
            subscription.setName(updatedSubscription.getName());
            subscription.setDescription(updatedSubscription.getDescription());
            return subscriptionRepository.save(subscription);
        }).orElseThrow(() -> new RuntimeException("Subscription not found with id " + id));
    }

    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public Subscription linkFeaturesToSubscription(Long subscriptionId, Set<Long> featureIds) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        List<Feature> features = featureRepository.findAllById(featureIds);
        subscription.getFeatures().addAll(features);  // Linking features to subscription

        return subscriptionRepository.save(subscription);  // Save updated subscription
    }

    // Optional: Method to get all features for a given subscription
    public Set<Feature> getFeaturesBySubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        return subscription.getFeatures();  // Return linked features
    }
}
