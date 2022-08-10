package com.example.grouperapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class StatsDTO {
    private Long totalRequest;
    private Long requestsInLastDay;
        private List<PercentageDTO> devicePercentages;
        private List<PercentageDTO> browserPercentages;
        private List<PercentageDTO> platformPercentages;
}
