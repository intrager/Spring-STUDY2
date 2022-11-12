package com.studyolle.infra.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processor count {}", processors);
        // CPU 또는 하는 작업들에 따라 달라짐
        executor.setCorePoolSize(processors); // 원래 튜브 개수(최적의 상태)
        executor.setMaxPoolSize(processors * 2); // 덤으로 더 만든 튜브 총 개수
        // 메모리에 따라 달라짐
        executor.setQueueCapacity(50); // 대기번호 50번까지(+ 이거에 maxPoolSize까지 찼는데 더 들어오면 reject)
        executor.setKeepAliveSeconds(60); // 더 만들어둔 튜브(maxPoolSize)들을 언제동안 살려뒀다가 정리할 것이냐
        executor.setThreadNamePrefix("AsyncExecutor-"); // 로깅할 때, 모니터링 할 때 보기 편함
        executor.initialize(); // 초기화 후 리턴해야함
        return executor;
    }
    /**
     * CorePoolSize, MaxPoolSize, QueueCapacity
     * 처리할 Task(이벤트)가 생겼을 때 "현재 일하고 있는 스레드 개수(active thread)"가
     * "코어 개수(core pool size)보다 적으면 남아있는 스레드 사용
     * "현재 일하고 있는 스레드 개수"가 코어 개수만큼 차있으면 "큐 용량(queue capacity)"이 찰 때까지 큐에 쌓아둠
     * 큐 용량이 다 차면, 코어 개수를 넘어서 "맥스 개수(max pool size)"에 이르기 전까지 새로운 스레드를 만들어 처리
     * 맥스 개수를 넘기면 Task를 처리하지 못 함
     */
}
