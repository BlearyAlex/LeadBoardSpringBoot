package com.alejandro.leadboardbackend.service.impl;

import com.alejandro.leadboardbackend.domain.dto.request.CreateLeadRequestDto;
import com.alejandro.leadboardbackend.domain.dto.request.UpdateLeadStatusRequestDto;
import com.alejandro.leadboardbackend.domain.dto.response.LeadResponseDto;
import com.alejandro.leadboardbackend.domain.entity.Lead;
import com.alejandro.leadboardbackend.exception.business.ResourceNotFoundException;
import com.alejandro.leadboardbackend.mapper.LeadMapper;
import com.alejandro.leadboardbackend.repository.LeadRepository;
import com.alejandro.leadboardbackend.service.LeadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;

    @Override
    public List<LeadResponseDto> getAllLeads() {
        return leadMapper.toDtoList(leadRepository.findAll());
    }

    @Override
    @Transactional
    public LeadResponseDto createLead(CreateLeadRequestDto request) {
        Lead lead = leadMapper.toEntity(request);
        Lead savedLead = leadRepository.save(lead);
        return leadMapper.toResponseDto(savedLead);
    }

    @Override
    @Transactional
    public LeadResponseDto editLead(Long leadId, UpdateLeadStatusRequestDto request) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead no encontrado con id:" + leadId));

        leadMapper.updateStatus(request, lead);

        return leadMapper.toResponseDto(lead);
    }

    @Override
    @Transactional
    public void deleteLead(Long leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead no encontrado con id:" + leadId));

        lead.setStatus(Lead.LeadStatus.ARCHIVED);
    }

    @Override
    public LeadResponseDto getLeadById(Long leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead no encontrado con id:" + leadId));

        return leadMapper.toResponseDto(lead);
    }
}
