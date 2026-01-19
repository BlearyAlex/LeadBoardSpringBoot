package com.alejandro.leadboardbackend.controller;

import com.alejandro.leadboardbackend.domain.dto.request.CreateLeadRequestDto;
import com.alejandro.leadboardbackend.domain.dto.request.UpdateLeadStatusRequestDto;
import com.alejandro.leadboardbackend.domain.dto.response.LeadResponseDto;
import com.alejandro.leadboardbackend.domain.entity.Lead;
import com.alejandro.leadboardbackend.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/leads")
@CrossOrigin("*")
public class LeadController {
    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping
    public ResponseEntity<List<LeadResponseDto>> getLeads(@RequestParam(required = false) Lead.LeadStatus status) {
        return ResponseEntity.ok(leadService.getLeadsByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponseDto> getById(@PathVariable Long id) {
        LeadResponseDto lead = leadService.getLeadById(id);
        return ResponseEntity.ok(lead);
    }

    @PostMapping
    public ResponseEntity<LeadResponseDto> create(@Valid @RequestBody CreateLeadRequestDto request) {
        LeadResponseDto lead = leadService.createLead(request);
        return ResponseEntity.ok(lead);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LeadResponseDto> edit(@PathVariable Long id, @Valid @RequestBody UpdateLeadStatusRequestDto request) {
        LeadResponseDto lead = leadService.editLead(id, request);
        return ResponseEntity.ok(lead);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        leadService.deleteLead(id);
        return ResponseEntity.noContent().build();
    }
}
