package com.eam.issue.service;

import com.eam.issue.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eam.issue.model.Issue;

import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    @Autowired
    private IssueRepository repository;

    public List<Issue> findAll(String tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    public Optional<Issue> findById(Long id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public Issue save(Issue issue) {
        return repository.save(issue);
    }

    public void deleteById(Long id, String tenantId) {
        Optional<Issue> issue = findById(id, tenantId);
        issue.ifPresent(repository::delete);
    }
}
