package com.example.fileupload.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

//            FileDTO에 담고
            FileDTO data = new FileDTO();
            data.setExcelfilePhoneNum((int) row.getCell(0).getNumericCellValue());
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


}
