package com.example.fileupload.scheduler;

import com.example.fileupload.file.FileService;
import com.example.fileupload.file.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final FileService fileService;
    @Scheduled(fixedDelay = 5000)
    private void scheduleTask() {
        String[] waitingFile = fileService.getWaitingTempFile();

        for (String file: waitingFile) {
//        엑셀에서 받은 정보를 담은 list를 DB에 저장
            UserVO[] userVOS = fileService.excelUpload(file);
        }

//        fileService.tempFileDelete();
//        fileService.sucessFileDelete();
        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
        log.info("5초 후 실행 => time : " + LocalTime.now());


    }

}
