package com.study.templateapi.api.feigntest.client;

import com.study.templateapi.api.health.dto.HealthCheckResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "http://localhost:8080", name = "helloClient")
public interface HelloClient {
    @GetMapping(value = "/api/health", consumes = "application/json")
    HealthCheckResponseDto healthCheck();
}
