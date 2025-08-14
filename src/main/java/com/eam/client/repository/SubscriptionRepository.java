package com.eam.client.repository;

import com.eam.client.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsBySubscriptionCode(String subscriptionCode);
}
