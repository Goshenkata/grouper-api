package com.example.grouperapi.repositories;

import com.example.grouperapi.model.entities.UserStats;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<UserStats, Long> {
    Long countAllByMomentAfter(Instant moment);
    @Query("SELECT DISTINCT s.browser FROM UserStats s")
    List<String> getAllByDisctinctBrowser();
    @Query("SELECT DISTINCT s.device FROM UserStats s")
    List<String> getAllByDisctinctDevice();
    @Query("SELECT DISTINCT s.platform FROM UserStats s")
    List<String> getAllByDisctinctPlatform();
    Long countAllByDevice(String device);
    Long countAllByBrowser(String browser);
    Long countAllByPlatform(String platform);
}
