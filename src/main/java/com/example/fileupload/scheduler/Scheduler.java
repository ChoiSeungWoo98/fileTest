package com.example.fileupload.scheduler;

import com.example.fileupload.file.FileDataVO;
import com.example.fileupload.file.FileService;
import com.example.fileupload.temporaryFile.TemporaryFileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http11.filters.SavedRequestInputFilter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final FileService fileService;
    private final String  DATA_DIRECTORY = "C:\\Temp";

    @Scheduled(fixedDelay = 1000)
    private void scheduleTask() {
        FileDataVO[] fileDataVO = fileService.findForExcelData();
        String[] tempNames = temporaryFileGetName();
//        temporaryFileInsert(tempNames);

        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
        log.info("10초 후 실행 => time : " + LocalTime.now());


    }

    public void temporaryFileInsert(String[] tempNames){
        File dir = new File(DATA_DIRECTORY);
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith("tmp");
            }
        };

        File[] files = dir.listFiles(fileFilter);
        List<TemporaryFileVO> dataList = new ArrayList<>();
        for (File file: files) {
            if(tempNames.length == 0){
                TemporaryFileVO temporaryFileVO = new TemporaryFileVO();
                temporaryFileVO.setTempFileName(file.getName());
                temporaryFileVO.setTempFileStatus("waiting");
                dataList.add(temporaryFileVO);
            }else {
                for (String temp: tempNames) {
                    if(!file.equals(temp)){
                        TemporaryFileVO temporaryFileVO = new TemporaryFileVO();
                        temporaryFileVO.setTempFileName(file.getName());
                        temporaryFileVO.setTempFileStatus("waiting");
                        dataList.add(temporaryFileVO);
                    }
                }
            }
        }
        if (dataList.size() != 0){
            fileService.createdTempFile(dataList);
        }
    }

    public String[] temporaryFileGetName(){
        String[] tempFileNames = fileService.selectedTempFile();
        return tempFileNames;
    }


}
