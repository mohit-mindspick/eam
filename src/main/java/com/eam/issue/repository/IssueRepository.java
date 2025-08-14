package com.eam.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.eam.issue.model.Issue;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findAllByTenantId(String tenantId);
    Optional<Issue> findByIdAndTenantId(Long id, String tenantId);
}

