package com.eam.auth.controller;

import com.eam.auth.model.Industry;
import com.eam.auth.service.IndustryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/industries")
public class IndustryController {

    private final IndustryService industryService;

    public IndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }

    @GetMapping
    public List<Industry> getAllIndustries() {
        return industryService.getAllIndustries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Industry> getIndustryById(@PathVariable Long id) {
        return industryService.getIndustryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Industry createIndustry(@RequestBody Industry industry) {
        return industryService.createIndustry(industry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Industry> updateIndustry(@PathVariable Long id, @RequestBody Industry industry) {
        try {
            return ResponseEntity.ok(industryService.updateIndustry(id, industry));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        industryService.deleteIndustry(id);
        return ResponseEntity.noContent().build();
    }
}
