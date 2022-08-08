package com.example.grouperapi.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class DataLoggingInterceptor implements HandlerInterceptor {

    private final UserAgentParser parser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String userAgent = request.getHeader("User-Agent");
        Capabilities capabilities = this.parser.parse(userAgent);

        // get number
        String userType;
        if (request.getHeader("Authorization") == null) {
            userType = "Anonymous";
        } else {
            userType = "User";
        }
        log.info(String.format("%s made request from %s %s on device %s running %s",
                    userType, capabilities.getBrowser(), capabilities.getBrowserMajorVersion(),
                    capabilities.getDeviceType(), capabilities.getPlatform()
                    ));


        return true;
    }

}
