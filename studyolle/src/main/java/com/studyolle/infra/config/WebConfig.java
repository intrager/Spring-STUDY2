package com.studyolle.infra.config;

import com.studyolle.modules.notification.NotificationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final NotificationInterceptor notificationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // enum을 String의 문자열, 배열로 바꿈
        List<String> staticResourcePath = Arrays.stream(StaticResourceLocation.values())
                        .flatMap(StaticResourceLocation::getPatterns)
                                .collect(Collectors.toList());
        staticResourcePath.add("/node_modules/**");

        registry.addInterceptor(notificationInterceptor)
                .excludePathPatterns("");
    }
}
