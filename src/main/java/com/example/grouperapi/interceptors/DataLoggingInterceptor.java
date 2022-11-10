package com.example.grouperapi.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.grouperapi.model.entities.UserStats;
import com.example.grouperapi.model.entities.enums.UserType;
import com.example.grouperapi.repositories.StatsRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

import java.time.Instant;

@Component
@AllArgsConstructor
@Slf4j
public class DataLoggingInterceptor implements HandlerInterceptor {
    private final StatsRepository userStatsRepository;
	private final Parser parser;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String userAgent = request.getHeader("User-Agent");
		Client client = parser.parse(userAgent);

		UserStats userStats = new UserStats();
		userStats.setMoment(Instant.now());
		userStats.setUserType(request.getHeader("Authorization") == null ? UserType.ANONYMOUS : UserType.USER);
		userStats.setBrowser(client.userAgent.family);
		userStats.setPlatform(client.os.family);
		log.debug(String.format("%s made request from %s running %s",
				userStats.getUserType().name(),
				userStats.getBrowser(),
				userStats.getPlatform()
		));
		userStatsRepository.save(userStats);
		return true;
	}

}
