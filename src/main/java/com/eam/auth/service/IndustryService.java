package com.eam.auth.service;

import com.eam.auth.model.Industry;
import com.eam.auth.repository.IndustryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IndustryService {

    private final IndustryRepository industryRepository;

    public IndustryService(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }

    public List<Industry> getAllIndustries() {
        return industryRepository.findAll();
    }

    public Optional<Industry> getIndustryById(Long id) {
        return industryRepository.findById(id);
    }

    public Industry createIndustry(Industry industry) {
        return industryRepository.save(industry);
    }

    public Industry updateIndustry(Long id, Industry updatedIndustry) {
        return industryRepository.findById(id)
                .map(industry -> {
                    industry.setIndustryCode(updatedIndustry.getIndustryCode());
                    industry.setName(updatedIndustry.getName());
                    return industryRepository.save(industry);
                }).orElseThrow(() -> new RuntimeException("Industry not found with id: " + id));
    }

    public void deleteIndustry(Long id) {
        industryRepository.deleteById(id);
    }
}
