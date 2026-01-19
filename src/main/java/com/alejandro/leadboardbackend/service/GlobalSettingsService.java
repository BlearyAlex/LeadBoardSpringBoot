package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.domain.entity.GlobalSettings;

public interface GlobalSettingsService {

    GlobalSettings create(GlobalSettings globalSettings);

    GlobalSettings update(Long id, GlobalSettings globalSettings);

    GlobalSettings getById(Long id);

    void delete(Long id);

    GlobalSettings getDefaultSettings();
}
