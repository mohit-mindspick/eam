package com.eam.auth.service;

import com.eam.auth.model.Industry;
import com.eam.auth.repository.IndustryRepository;
import com.eam.i18n.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class IndustryService {

    private final IndustryRepository industryRepository;
    private final TranslationService translationService;

    public List<Industry> getAllIndustries() {
        List<Industry> industries = industryRepository.findAll();
        // Apply translations to each industry
        return industries.stream()
                .map(translationService::applyTranslations)
                .toList();
    }

    public Optional<Industry> getIndustryById(Long id) {
        Optional<Industry> industryOpt = industryRepository.findById(id);
        if (industryOpt.isPresent()) {
            Industry industry = industryOpt.get();
            // Apply translations to the industry
            Industry translatedIndustry = translationService.applyTranslations(industry);
            return Optional.of(translatedIndustry);
        }
        return Optional.empty();
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
