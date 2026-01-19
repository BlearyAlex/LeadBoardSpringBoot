package com.alejandro.leadboardbackend.mapper;

import com.alejandro.leadboardbackend.domain.dto.response.ProjectImageResponseDto;
import com.alejandro.leadboardbackend.domain.entity.ProjectImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectImageMapper {

    ProjectImageResponseDto toDto(ProjectImage image);
    List<ProjectImageResponseDto> toDtoList(List<ProjectImage> images);
}
