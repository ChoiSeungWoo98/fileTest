package com.example.fileupload.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/excel/*")
public class FileController {
    private final FileService fileService;

    @PostMapping("/read")
    public String readExcel(@RequestParam("file") MultipartFile file, Model model)
            throws IOException {
//        엑셀의 모든 정보를 담기위한 리스트
        List<FileDTO> dataList = new ArrayList<>();

//        전화번호를 담기위한 변수
        String cellPN = null;

//        확장자를 가져와 엑셀 파일인지 구분하기 위해 사용
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }
//        하나의 엑셀파일을 담는 용도
        Workbook workbook = null;

        if (extension.equals("xlsx")) {
//            엑셀 2007버전 이상 일때 XSSFWorkbook
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
//            엑셀 97~03버전일때 HSSFWorkbook
            workbook = new HSSFWorkbook(file.getInputStream());
        }

//        workbook안에 (엑셀)시트 생성
        Sheet worksheet = workbook.getSheetAt(0);

//        행 개수 만큼 반복문을 돌려 데이터를 가져온다
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

//            한 행씩 가져와 담고
            Row row = worksheet.getRow(i);

//            가져온 데이터를 전화번호 형식으로 변환
            row.getCell(0).setCellFormula(String.valueOf(worksheet.getRow(i).getCell(0)));
            cellPN = convertTelNo("0"+row.getCell(0));

//            FileDTO에 담고
            FileDTO data = new FileDTO();
            data.setExcelfilePhoneNum(cellPN);
            data.setExcelfileName(row.getCell(1).getStringCellValue());
            data.setExcelfileEmail(row.getCell(2).getStringCellValue());

//            엑셀 전체 정보를 담을 list에 추가
            dataList.add(data);
        }
//        엑셀에서 받은 정보를 담은 list를 DB에 저장
        fileService.excelUpload(dataList);

        model.addAttribute("datas", dataList);

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

            if (mobTelNo.length() == 11) {
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


}


