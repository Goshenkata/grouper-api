package com.example.grouperapi.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;

import com.example.grouperapi.model.entities.UserStats;
import com.example.grouperapi.model.entities.enums.UserType;
import com.example.grouperapi.repositories.StatsRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Component
@AllArgsConstructor
@Slf4j
public class DataLoggingInterceptor implements HandlerInterceptor {
    private final StatsRepository userStatsRepository;

    private final UserAgentParser parser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String userAgent = request.getHeader("User-Agent");
        Capabilities capabilities = this.parser.parse(userAgent);

        UserStats userStats = new UserStats();
        userStats.setMoment(Instant.now());
        userStats.setUserType(request.getHeader("Authorization") == null ? UserType.ANONYMOUS : UserType.USER);
        userStats.setBrowser(capabilities.getBrowser());
        userStats.setDevice(capabilities.getDeviceType());
        userStats.setPlatform(capabilities.getPlatform());
        log.debug(String.format("%s made request from %s on device %s running %s",
                userStats.getUserType().name(),
                userStats.getBrowser(),
                userStats.getDevice(),
                userStats.getPlatform()
        ));
        userStatsRepository.save(userStats);
        return true;
    }

}
