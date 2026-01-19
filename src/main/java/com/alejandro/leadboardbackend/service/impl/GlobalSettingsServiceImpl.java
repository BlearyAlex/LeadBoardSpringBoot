package com.alejandro.leadboardbackend.service.impl;

import com.alejandro.leadboardbackend.domain.entity.GlobalSettings;
import com.alejandro.leadboardbackend.exception.business.ResourceNotFoundException;
import com.alejandro.leadboardbackend.repository.GlobalSettingsRepository;
import com.alejandro.leadboardbackend.service.GlobalSettingsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GlobalSettingsServiceImpl implements GlobalSettingsService {

    private final GlobalSettingsRepository globalSettingsRepository;

    public GlobalSettingsServiceImpl(GlobalSettingsRepository globalSettingsRepository) {
        this.globalSettingsRepository = globalSettingsRepository;
    }


    @Override
    @Transactional
    public GlobalSettings create(GlobalSettings globalSettings) {
        return globalSettingsRepository.save(globalSettings);
    }

    @Override
    @Transactional
    public GlobalSettings update(Long id, GlobalSettings request) {
        Optional<GlobalSettings> existingSettings = globalSettingsRepository.findById(id);

        if (existingSettings.isPresent()) {
            GlobalSettings settingsUpdate = existingSettings.get();
            settingsUpdate.setContactPhone(request.getContactPhone());
            settingsUpdate.setContactEmail(request.getContactEmail());
            settingsUpdate.setOfficeAddress(request.getOfficeAddress());
            settingsUpdate.setInstagramUrl(request.getInstagramUrl());
            settingsUpdate.setLinkedinUrl(request.getLinkedinUrl());
            settingsUpdate.setAboutMeSummary(request.getAboutMeSummary());
            settingsUpdate.setCvResumeUrl(request.getCvResumeUrl());

            return globalSettingsRepository.save(settingsUpdate);
        } else {
            throw new ResourceNotFoundException("GlobalSettings con id: " + id + "no encontrado");
        }
    }

    @Override
    public GlobalSettings getById(Long id) {
        return globalSettingsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GlobalSettings with ID " + id + " not found."));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!globalSettingsRepository.existsById(id)) {
            throw new ResourceNotFoundException("GlobalSettings with ID " + id + " not found.");
        }
        globalSettingsRepository.deleteById(id);
    }

    @Override
    public GlobalSettings getDefaultSettings() {
        return globalSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No global settings found."));
    }
}
