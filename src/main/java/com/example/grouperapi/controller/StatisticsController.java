package com.example.grouperapi.controller;

import com.example.grouperapi.model.dto.StatsDTO;
import com.example.grouperapi.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class StatisticsController {
    StatisticsService statisticsService;

    @GetMapping("stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatsDTO> stats() {
        return ResponseEntity.ok(statisticsService.getStats());
    }

}
