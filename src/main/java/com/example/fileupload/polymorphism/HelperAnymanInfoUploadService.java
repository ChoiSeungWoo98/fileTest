package com.example.fileupload.polymorphism;

import com.example.fileupload.file.FileDataVO;
import com.example.fileupload.file.FileMapper;
import com.example.fileupload.file.HelperAnymanInfoVO;
import com.example.fileupload.file.SheetVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HelperAnymanInfoUploadService {
    @Resource
    ObjectMapper objectMapper;
    @Resource
    FileMapper fileMapper;
    final String  DATA_DIRECTORY = "C:"+ File.separator+"Temp";

    public List<HelperAnymanInfoVO> fileDataGet(String tempFileName, int sheetIndex) {
        ArrayList<String> overName = new ArrayList<>();
        List<HelperAnymanInfoVO> dataList = new ArrayList<>();
        FileDataVO fileDataVO = new FileDataVO();

        SheetVO sheetVO = new SheetVO();
        sheetVO.setSheetFileName(tempFileName.substring(5));
        sheetVO.setSheetStatus("대기");

        String extension = FilenameUtils.getExtension(tempFileName);

        Workbook workbook = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(DATA_DIRECTORY + File.separator + tempFileName);
            workbook = extension.equals("xlsx")
                    ? new XSSFWorkbook(fileInputStream)
                    : new HSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            e.printStackTrace();
        }

        workbook.getSheetAt(sheetIndex).forEach( row -> {
            try {
                if(row.getRowNum() != 0){
                    HelperAnymanInfoVO helperAnymanInfoVO = new HelperAnymanInfoVO();
                    helperAnymanInfoVO.setHelperAnymanCi(row.getCell(0).getStringCellValue());
                    helperAnymanInfoVO.setHelperAnymanUserId((int) row.getCell(1).getNumericCellValue());
                    helperAnymanInfoVO.setHelperAnymanHelperId((int) row.getCell(2).getNumericCellValue());
                    if(!(row.getCell(3) == null)){
                        helperAnymanInfoVO.setHelperAnymanUserName(String.valueOf(row.getCell(3).getCellStyle()));
                    }
                    helperAnymanInfoVO.setHelperAnymanProfilePhoto(row.getCell(4).getStringCellValue());
                    helperAnymanInfoVO.setHelperAnymanLicenses(row.getCell(5).getStringCellValue());
                    helperAnymanInfoVO.setHelperAnymanExperience(row.getCell(6).getStringCellValue());

                    String overId = fileMapper.helperAnymanKeyCheck(helperAnymanInfoVO);
                    if(overId != null){ overName.add(overId); }

                    dataList.add(helperAnymanInfoVO);
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        try {
            if (!overName.isEmpty()){
                fileDataVO.setConsequence("실패.. 중복 데이터가 있습니다." + overName);
                fileDataVO.setOperationStatus("failed");
                fileDataVO.setTempFileName(tempFileName);
                fileMapper.excelDataUpdate(fileDataVO);
                sheetVO.setSheetStatus("실패.. 중복 데이터가 있습니다." + overName);
                dataList.clear();
                throw new Exception("중복 데이터가 있습니다.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        sheetVO.setSheetCount(dataList.size());
        String json = "";
        try {
            json = objectMapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        };
        sheetVO.setSheetData(json);

        fileMapper.sheetUpload(sheetVO);

        return dataList;
    }

}
