package com.eam.auth.repository;

import com.eam.auth.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IndustryRepository extends JpaRepository<Industry, Long> {
    Optional<Industry> findByIndustryCode(String industryCode);
}