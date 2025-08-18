package com.eam.auth.service;

import com.eam.auth.model.Location;
import com.eam.auth.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    public Location updateLocation(Long id, Location updatedLocation) {
        Location existing = getLocationById(id);
        existing.setName(updatedLocation.getName());
        existing.setTenantId(updatedLocation.getTenantId());
        existing.setParent(updatedLocation.getParent());
        return locationRepository.save(existing);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public Location getLocation(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    public List<Location> getAllLocations(Long tenantId) {
        return locationRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Location getLocationTree(Long tenantId, Long parentId) {
        List<Object[]> flatList = locationRepository.findLocationTree(tenantId, parentId);

        Map<Long, Location> locationMap = new HashMap<>();
        for (Object[] row : flatList) {
            Location loc = new Location();
            loc.setId(((Number) row[0]).longValue());
            loc.setName((String) row[1]);
            loc.setTenantId(((Number) row[2]).longValue());
            locationMap.put(loc.getId(), loc);
        }

        Location root = null;
        for (Object[] row : flatList) {
            Long id = ((Number) row[0]).longValue();
            Long parent = row[3] != null ? ((Number) row[3]).longValue() : null;

            if (parent != null && locationMap.containsKey(parent)) {
                locationMap.get(parent).getChildren().add(locationMap.get(id));
            } else {
                root = locationMap.get(id);
            }
        }

        return root;
    }

    public Location getLocationTree(Long parentId) {
        List<Location> flatList = locationRepository.findLocationTree(parentId);

        // Build a map for quick access
        Map<Long, Location> locationMap = flatList.stream()
                .collect(Collectors.toMap(Location::getId, loc -> loc));

        Location root = null;

        for (Location loc : flatList) {
            if (loc.getParent() != null && locationMap.containsKey(loc.getParent().getId())) {
                locationMap.get(loc.getParent().getId()).getChildren().add(loc);
            } else {
                root = loc; // This is the parentId location
            }
        }
        return root;
    }
}