package com.study.templateapi.api.feigntest.controller;

import com.study.templateapi.api.feigntest.client.HelloClient;
import com.study.templateapi.api.health.dto.HealthCheckResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthFeignTestController {
    private final HelloClient helloClient;

    @GetMapping("/health/feign-test")
    public ResponseEntity<HealthCheckResponseDto> healthCheckTest() {
        HealthCheckResponseDto healthCheckResponseDto = helloClient.healthCheck();
        return ResponseEntity.ok(healthCheckResponseDto);
    }
}
