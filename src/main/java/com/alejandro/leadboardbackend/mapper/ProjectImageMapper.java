package com.alejandro.leadboardbackend.mapper;

import com.alejandro.leadboardbackend.dto.ProjectImageResponseDto;
import com.alejandro.leadboardbackend.dto.ProjectResponseDto;
import com.alejandro.leadboardbackend.model.ProjectImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectImageMapper {

    ProjectImageResponseDto toDto(ProjectImage image);
    List<ProjectImageResponseDto> toDtoList(List<ProjectImage> images);
}
