package com.example.fileupload.scheduler;

import com.example.fileupload.file.FileService;
import com.example.fileupload.file.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final FileService fileService;
    private final String  DATA_DIRECTORY = "C:\\Temp";

    @Scheduled(fixedDelay = 5000)
    private void scheduleTask() throws IOException {
//        String[] tempNames = getTempName();
//        String[] waitingTempFile = fileService.getWaitingTempFile();
//        log.info("----------------------------------------------------");
//        log.info(Arrays.stream(waitingTempFile).iterator().next());
//        log.info("----------------------------------------------------");
//        FileDataVO[] fileDataVO = fileService.findForExcelData(waitingTempFile);
//        for (String wating: waitingTempFile) {
////            wating = wating.substring(0,wating.lastIndexOf(".")) + ".xlsx";
////            MultipartFile file = wating;
////            log.info(wating);
//            Workbook workbook = new XSSFWorkbook(wating);
//
//            for (FileDataVO fileDataVO1 : fileDataVO) {
//                if(fileDataVO1.getTempFileName().equals(wating)){
//                    FileVO[] fileVOS = fileService.excelUpload(workbook,fileDataVO1);
//                }
//
//            }
//        }
        String[] waitingFile = fileService.getWaitingTempFile();

        for (String file: waitingFile) {
            FileInputStream fileInputStream = new FileInputStream(DATA_DIRECTORY + File.separator + file);

            String extension = FilenameUtils.getExtension(file);
            Workbook workbook = extension.equals("xlsx")
                    ? new XSSFWorkbook(fileInputStream)
                    : new HSSFWorkbook(fileInputStream);
//        엑셀에서 받은 정보를 담은 list를 DB에 저장
            FileVO[] fileVO = fileService.excelUpload(workbook, file);
            log.info("---------------------------------------------------------------------------------------");
            log.info(String.valueOf(Arrays.stream(fileVO).iterator().next()));
            log.info("---------------------------------------------------------------------------------------");
//            model.addAttribute("datas", fileVO);
        }

        fileService.tempFileDelete();
        fileService.sucessFileDelete();
        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
        log.info("5초 후 실행 => time : " + LocalTime.now());


    }
//    public void temporaryFileUpdate(String[] tempNames){
//        List<File> files = new ArrayList<>();
//        for (File file: allFileGet("xls")) {
//            files.add(file);
//        }
//        for (File file: allFileGet("xlsx")) {
//            files.add(file);
//        }
//
//        List<FileDataVO> dataList = new ArrayList<>();
//        for (File file: files) {
//            if(tempNames.length == 0){
//                FileDataVO fileDataVO = new FileDataVO();
//                fileDataVO.setTempFileName(file.getName());
//                fileDataVO.setConsequence("waiting");
//                dataList.add(fileDataVO);
//            }else {
//                for (String temp: tempNames) {
//                    if(!file.equals(temp)){
//                        FileDataVO fileDataVO = new FileDataVO();
//                        fileDataVO.setTempFileName(file.getName());
//                        fileDataVO.setConsequence("waiting");
//                        dataList.add(fileDataVO);
//                    }
//                }
//            }
//        }
//        if (dataList.size() != 0){
////            fileService.tempFileNameAdd(dataList);
//        }
//    }
//    public String FilesRename(String fileName) {
//        String type = "tmp";
//        File[] tempNames = allFileGet(type);
//        Random r = new Random();
//        String newTmp = "";
//
//        for (File tmp : tempNames) {
//            Path file = Paths.get(tmp.getPath());
//            log.info("---------------------------------------------");
//            log.info(String.valueOf(file));
////            모든 파일 이름과 타입 업로드
//            String original = fileService.getFileOriginalNameAndType(fileName);
//
//
//            newTmp = r.nextInt(100) + original;
//            log.info("---------------------------------------------");
//            log.info(String.valueOf(original));
//            log.info("---------------------------------------------");
//            log.info(newTmp);
//            Path newFile = Paths.get(DATA_DIRECTORY + "\\" + newTmp);
//
//            try {
////                Path newFilePath = Files.createFile(newFile);
//                Path newFilePath = Files.copy(file, newFile, StandardCopyOption.REPLACE_EXISTING);
//                tmp.delete();
//                log.info("---------------------------------------------");
//                log.info(String.valueOf(newFilePath));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//        return newTmp;
//    }

}
