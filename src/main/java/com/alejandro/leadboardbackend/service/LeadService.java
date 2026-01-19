package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.domain.dto.request.CreateLeadRequestDto;
import com.alejandro.leadboardbackend.domain.dto.request.UpdateLeadStatusRequestDto;
import com.alejandro.leadboardbackend.domain.dto.response.LeadResponseDto;

import java.util.List;

public interface LeadService {
    List<LeadResponseDto> getAllLeads();

    LeadResponseDto createLead(CreateLeadRequestDto request);

    LeadResponseDto editLead(Long leadId, UpdateLeadStatusRequestDto request);

    void deleteLead(Long leadId);

    LeadResponseDto getLeadById(Long leadId);
}
