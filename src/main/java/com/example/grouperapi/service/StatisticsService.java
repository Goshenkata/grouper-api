package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.PercentageDTO;
import com.example.grouperapi.model.dto.StatsDTO;
import com.example.grouperapi.repositories.StatsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class StatisticsService {
    StatsRepository statsRepository;

    public StatsDTO getStats() {
        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setTotalRequest(statsRepository.count());
        statsDTO.setRequestsInLastDay(statsRepository.countAllByMomentAfter(Instant.now().minus(1, ChronoUnit.DAYS)));

        //browser percentages
        List<String> browsers = statsRepository.getAllByDisctinctBrowser();
        List<PercentageDTO> browserPercentages = new ArrayList<>();
        for (String browser : browsers) {
            Long countAllByBrowser = statsRepository.countAllByBrowser(browser);
            browserPercentages.add(new PercentageDTO(browser, countAllByBrowser));
        }
        statsDTO.setBrowserPercentages(browserPercentages);

        //devices
        List<String> devices = statsRepository.getAllByDisctinctDevice();
        List<PercentageDTO> devicePercentages = new ArrayList<>();
        for (String device : devices) {
            Long countAllByDevice = statsRepository.countAllByDevice(device);
            devicePercentages.add(new PercentageDTO(device, countAllByDevice));
        }
        statsDTO.setDevicePercentages(devicePercentages);

        //platform
        List<String> platforms = statsRepository.getAllByDisctinctPlatform();
        List<PercentageDTO> platformPercentages = new ArrayList<>();
        for (String platform : platforms) {
            Long countAllByPlatform = statsRepository.countAllByPlatform(platform);
            platformPercentages.add(new PercentageDTO(platform, countAllByPlatform));
        }
        statsDTO.setPlatformPercentages(platformPercentages);
        return statsDTO;
    }
}
