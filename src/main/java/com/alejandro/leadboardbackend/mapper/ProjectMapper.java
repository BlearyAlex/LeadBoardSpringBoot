package com.alejandro.leadboardbackend.mapper;


import com.alejandro.leadboardbackend.dto.request.ProjectRequestDto;
import com.alejandro.leadboardbackend.dto.response.ProjectResponseDto;
import com.alejandro.leadboardbackend.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProjectImageMapper.class})
public interface ProjectMapper {

    @Mapping(target = "galleryUrls", source = "gallery")
    ProjectResponseDto toResponseDto(Project project);

    Project toEntity(ProjectRequestDto projectRequestDto);

    List<ProjectResponseDto> toDtoList(List<Project> projects);

    void updateEntityFromDto(ProjectRequestDto projectRequestDto, @MappingTarget Project project);

}
