package com.alejandro.leadboardbackend.controller;

import com.alejandro.leadboardbackend.domain.entity.GlobalSettings;
import com.alejandro.leadboardbackend.service.GlobalSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/settings")
public class GlobalSettingsController {

    private final GlobalSettingsService globalSettingsService;

    public GlobalSettingsController(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalSettings> getSettings(@PathVariable Long id) {
        GlobalSettings settings = globalSettingsService.getById(id);
        return ResponseEntity.ok(settings);
    }

    @PostMapping
    public ResponseEntity<GlobalSettings> createSettings(@RequestBody GlobalSettings globalSettings) {
        GlobalSettings createdSettings = globalSettingsService.create(globalSettings);
        return ResponseEntity.ok(createdSettings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalSettings> updateSettings(@PathVariable Long id, @RequestBody GlobalSettings globalSettings) {
        GlobalSettings updatedSettings = globalSettingsService.update(id, globalSettings);
        return ResponseEntity.ok(updatedSettings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSettings(@PathVariable Long id) {
        globalSettingsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/default")
    public ResponseEntity<GlobalSettings> getDefaultSettings() {
        GlobalSettings defaultSettings = globalSettingsService.getDefaultSettings();
        return ResponseEntity.ok(defaultSettings);
    }
}
