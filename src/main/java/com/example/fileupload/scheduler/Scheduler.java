package com.example.fileupload.scheduler;

import com.example.fileupload.file.FileDataVO;
import com.example.fileupload.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final FileService fileService;

    @Scheduled(fixedDelay = 10000)
    public void scheduleTask() {
        FileDataVO[] fileDataVO = fileService.findForExcelData();
        log.info(Arrays.toString(fileDataVO));
        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
        log.info("10초 후 실행 => time : " + LocalTime.now());

    }
}
