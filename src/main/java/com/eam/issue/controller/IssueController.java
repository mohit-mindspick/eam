package com.eam.issue.controller;

import com.eam.issue.model.Issue;
import com.eam.issue.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*")
@Tag(name = "Issue Management", description = "APIs for managing issues and incidents including creation, retrieval, updates, and deletion")
public class IssueController {

    @Autowired
    private IssueService service;

    @GetMapping
    @Operation(
        summary = "Get All Issues",
        description = "Retrieves all issues for a specific tenant"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Issues retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Issue.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid tenant ID"
        )
    })
    public List<Issue> getAllIssues(
        @Parameter(description = "Tenant ID for multi-tenancy", required = true, example = "tenant123")
        @RequestHeader("X-Tenant-ID") String tenantId) {
        return service.findAll(tenantId);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get Issue by ID",
        description = "Retrieves a specific issue by its ID for a given tenant"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Issue found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Issue.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Issue not found"
        )
    })
    public ResponseEntity<Issue> getIssueById(
        @Parameter(description = "Issue ID", required = true, example = "1")
        @PathVariable Long id, 
        @Parameter(description = "Tenant ID for multi-tenancy", required = true, example = "tenant123")
        @RequestHeader("X-Tenant-ID") String tenantId) {
        Optional<Issue> issue = service.findById(id, tenantId);
        return issue.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
        summary = "Create New Issue",
        description = "Creates a new issue for the specified tenant"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Issue created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Issue.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid issue data"
        )
    })
    public Issue createIssue(
        @Parameter(description = "Issue object to create", required = true)
        @RequestBody Issue issue, 
        @Parameter(description = "Tenant ID for multi-tenancy", required = true, example = "tenant123")
        @RequestHeader("X-Tenant-ID") String tenantId) {
        issue.setTenantId(tenantId);
        return service.save(issue);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update Issue",
        description = "Updates an existing issue by ID for the specified tenant"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Issue updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Issue.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Issue not found"
        )
    })
    public ResponseEntity<Issue> updateIssue(
        @Parameter(description = "Issue ID to update", required = true, example = "1")
        @PathVariable Long id, 
        @Parameter(description = "Updated issue data", required = true)
        @RequestBody Issue issue, 
        @Parameter(description = "Tenant ID for multi-tenancy", required = true, example = "tenant123")
        @RequestHeader("X-Tenant-ID") String tenantId) {
        Optional<Issue> existingIssue = service.findById(id, tenantId);
        if (existingIssue.isPresent()) {
            issue.setId(id);
            issue.setTenantId(tenantId);
            return ResponseEntity.ok(service.save(issue));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete Issue",
        description = "Deletes an issue by ID for the specified tenant"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Issue deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Issue not found"
        )
    })
    public ResponseEntity<Void> deleteIssue(
        @Parameter(description = "Issue ID to delete", required = true, example = "1")
        @PathVariable Long id, 
        @Parameter(description = "Tenant ID for multi-tenancy", required = true, example = "tenant123")
        @RequestHeader("X-Tenant-ID") String tenantId) {
        if (service.findById(id, tenantId).isPresent()) {
            service.deleteById(id, tenantId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
