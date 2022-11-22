package com.example.fileupload.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@RequestMapping("/excel/*")
@Slf4j
public class FileController {
    private final FileService fileService;

    @PostMapping("/read")
    public String readExcel(@RequestParam("file") MultipartFile file, Model model)
            throws IOException {
//        엑셀의 모든 정보를 담기위한 리스트
        List<FileVO> dataList = new ArrayList<>();

        //현재시각을 가져옴
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = simpleDateFormat.format(now);

//        파일의 데이터
        FileDataVO fileDataVO = new FileDataVO();
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        fileDataVO.setExcelfiledatName(file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")));
        fileDataVO.setExcelfiledataType(fileType);
        fileDataVO.setExcelfiledataUploadTime(nowTime);
        fileDataVO.setExcelfiledataOperationStatus("false");

//        전화번호를 담기위한 변수
        String cellPN = null;

//        확장자를 가져와 엑셀 파일인지 구분하기 위해 사용
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            fileDataVO.setExcelfiledataResult("실패.. 엑셀파일만 업로드 해주세요.");
            fileService.excelDataUpload(fileDataVO);
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }
//        하나의 엑셀파일을 담는 용도
        Workbook workbook = extension.equals("xlsx")
                ? new XSSFWorkbook(file.getInputStream())
                : new HSSFWorkbook(file.getInputStream());

//        workbook안에 (엑셀)시트 읽어옴
        Sheet worksheet = workbook.getSheetAt(0);

        ArrayList<String> overName = new ArrayList<>();
//        행 개수 만큼 반복문을 돌려 데이터를 가져온다
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

//            한 행씩 가져와 담고
            Row row = worksheet.getRow(i);

//            가져온 데이터를 전화번호 형식으로 변환
            row.getCell(0).setCellFormula(String.valueOf(worksheet.getRow(i).getCell(0)));
            cellPN = convertTelNo("0"+row.getCell(0));

            if(cellPN.equals("failed")){
                fileDataVO.setExcelfiledataResult("실패.."+i+"번째 정보에 전화번호 형식이 잘못 되었습니다.");
                fileService.excelDataUpload(fileDataVO);
                throw new IOException("전화번호 형식이 잘못 되었습니다.");
            }

//            FileVO에 담고
            FileVO data = new FileVO();
            data.setExcelfilePhoneNum(cellPN);
            data.setExcelfileName(row.getCell(1).getStringCellValue());
            data.setExcelfileEmail(row.getCell(2).getStringCellValue());

            if(fileService.pkKeyCheck(data) != null){
                overName.add(fileService.pkKeyCheck(data));
            }

//            엑셀 전체 정보를 담을 list에 추가
            dataList.add(data);
        }


        if (overName.size() != 0){
            fileDataVO.setExcelfiledataResult("실패.. 중복 데이터가 있습니다." + overName);
            fileService.excelDataUpload(fileDataVO);
            throw new IOException("중복 데이터가 있습니다.");
        }
//        엑셀에서 받은 정보를 담은 list를 DB에 저장
        fileService.excelUpload(dataList);

        model.addAttribute("datas", dataList);

//        처리된 시각
        now = new Date();
        nowTime = simpleDateFormat.format(now);
        fileDataVO.setExcelfiledataProcessingTime(nowTime);
        fileDataVO.setExcelfiledataOperationStatus("true");
        fileDataVO.setExcelfiledataResult("성공");
        fileService.excelDataUpload(fileDataVO);

        return "excelList";

    }

    @GetMapping("/main")
    public String goMain(){
        return "excel";
    }


    public static String convertTelNo(String src) {

        String mobTelNo = src;

        if (mobTelNo != null) {
            // 일단 기존 - 전부 제거
            mobTelNo = mobTelNo.replaceAll(Pattern.quote("-"), "");

            if(mobTelNo.length() != 11 && mobTelNo.length() != 8 && mobTelNo.length() != 10 && mobTelNo.length() != 9){
                return "failed";
            } else if (mobTelNo.length() == 11) {
                // 010-1234-1234
                mobTelNo = mobTelNo.substring(0, 3) + "-" + mobTelNo.substring(3, 7) + "-" + mobTelNo.substring(7);

            } else if (mobTelNo.length() == 8) {
                // 1588-1234
                mobTelNo = mobTelNo.substring(0, 4) + "-" + mobTelNo.substring(4);
            } else {
                if (mobTelNo.startsWith("02")) {
                    mobTelNo = mobTelNo.substring(0, 2) + "-" + mobTelNo.substring(2, 5) + "-" + mobTelNo.substring(5);
                } else {
                    mobTelNo = mobTelNo.substring(0, 3) + "-" + mobTelNo.substring(3, 6) + "-" + mobTelNo.substring(6);
                }
            }

        }

        return mobTelNo;
    }


//    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
//// @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}") // 문자열 milliseconds 사용 시
//    public void scheduledFixedDelayTask() throws InterruptedException {
//        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
//        log.info("10초 후 실행 => time : " + LocalTime.now());
//    }
}


