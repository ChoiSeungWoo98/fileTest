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
public class FileController {
    private final FileService fileService;

    @ResponseBody
    @PostMapping("/excel/read")
    public String readExcel(@RequestParam("file") MultipartFile file, Model model)
            throws IOException { // 2

        List<FileDTO> dataList = new ArrayList<>();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); //

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

            Row row = worksheet.getRow(i);

            FileDTO data = new FileDTO();
            data.setUserPhoneNum((int) row.getCell(0).getNumericCellValue());
            data.setUserName(row.getCell(1).getStringCellValue());
            data.setUserEmail(row.getCell(2).getStringCellValue());

            dataList.add(data);
        }
//        fileService.excelUpload(dataList);

        model.addAttribute("datas", dataList); // 5

        return "excelList";

    }

    @GetMapping("/main")
    public String goMain(){
        return "excel";
    }


}
