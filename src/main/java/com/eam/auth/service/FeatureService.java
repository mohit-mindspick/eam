package com.eam.auth.service;

import com.eam.auth.model.Feature;
import com.eam.auth.model.Permission;
import com.eam.auth.repository.FeatureRepository;
import com.eam.auth.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;
    private final PermissionRepository permissionRepository;

    public List<Feature> getAllFeatures() {
        return featureRepository.findAll();
    }

    public Feature getFeatureById(Long id) {
        return featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found"));
    }

    public Feature createFeature(Feature feature) {
        return featureRepository.save(feature);
    }

    public Feature updateFeature(Long id, Feature updated) {
        Feature existing = getFeatureById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return featureRepository.save(existing);
    }

    public void deleteFeature(Long id) {
        featureRepository.deleteById(id);
    }

    public Feature addPermissionsToFeature(Long featureId, List<Long> permissionIds) {
        Feature feature = getFeatureById(featureId);
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        feature.getPermissions().addAll(permissions);
        return featureRepository.save(feature);
    }
}