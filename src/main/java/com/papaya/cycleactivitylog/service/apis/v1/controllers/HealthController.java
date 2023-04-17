package com.papaya.cycleactivitylog.service.apis.v1.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO remove once devops switch health endpoint check to /scheduler/health
@RequiredArgsConstructor
@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<Object> get() {
        return ResponseEntity.ok().build();
    }
}
