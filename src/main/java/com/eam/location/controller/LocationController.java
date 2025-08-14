package com.eam.location.controller;

import com.eam.location.model.Location;
import com.eam.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.createLocation(location));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        return ResponseEntity.ok(locationService.updateLocation(id, location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocation(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Location>> getAllLocations(@PathVariable Long clientId) {
        return ResponseEntity.ok(locationService.getAllLocations(clientId));
    }

    @GetMapping("/tree/{clientId}/{parentId}")
    public ResponseEntity<Location> getLocationTree(@PathVariable Long clientId, @PathVariable Long parentId) {
        return ResponseEntity.ok(locationService.getLocationTree(clientId, parentId));
    }

    @GetMapping("/{parentId}/tree")
    public ResponseEntity<Location> getLocationTree(@PathVariable Long parentId) {
        Location locationTree = locationService.getLocationTree(parentId);
        if (locationTree == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locationTree);
    }
}