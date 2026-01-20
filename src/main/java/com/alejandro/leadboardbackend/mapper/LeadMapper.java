package com.alejandro.leadboardbackend.mapper;

import com.alejandro.leadboardbackend.domain.dto.request.CreateLeadRequestDto;
import com.alejandro.leadboardbackend.domain.dto.request.UpdateLeadStatusRequestDto;
import com.alejandro.leadboardbackend.domain.dto.response.LeadResponseDto;
import com.alejandro.leadboardbackend.domain.entity.Lead;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeadMapper {

    LeadResponseDto toResponseDto(Lead lead);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receivedAt", ignore = true)
    @Mapping(target = "status", constant = "NEW")
    Lead toEntity(CreateLeadRequestDto createLeadRequestDto);

    List<LeadResponseDto> toDtoList(List<Lead> leads);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status",  expression = "java(Lead.LeadStatus.valueOf(request.getStatus().toUpperCase()))")
    void updateStatus(UpdateLeadStatusRequestDto request, @MappingTarget Lead lead);
}
