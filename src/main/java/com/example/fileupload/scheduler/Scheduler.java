package com.example.fileupload.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@Slf4j
public class Scheduler {

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void scheduleTask() {
        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
        log.info("10초 후 실행 => time : " + LocalTime.now());
    }
}
