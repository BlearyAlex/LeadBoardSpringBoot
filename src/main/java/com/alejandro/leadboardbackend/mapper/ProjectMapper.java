package com.alejandro.leadboardbackend.mapper;


import com.alejandro.leadboardbackend.dto.ProjectRequestDto;
import com.alejandro.leadboardbackend.dto.ProjectResponseDto;
import com.alejandro.leadboardbackend.model.Project;
import com.alejandro.leadboardbackend.model.ProjectImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponseDto toResponseDto(Project project);

    Project toEntity(ProjectRequestDto projectRequestDto);

    List<ProjectResponseDto> toDtoList(List<Project> projects);

    void updateEntityFromDto(ProjectRequestDto projectRequestDto, @MappingTarget Project project);

}
