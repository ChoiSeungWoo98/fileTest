package com.example.fileupload.polymorphism;

import com.example.fileupload.file.FileDataVO;
import com.example.fileupload.file.FileMapper;
import com.example.fileupload.file.HelperVO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class HelperPersonalInfoUploadInternalService {
    @Resource
    FileMapper fileMapper;
    final int TEL_CEL_NUMBER = 4;
    final String  DATA_DIRECTORY = "C:"+ File.separator+"Temp";

//    public HelperExcel(FileMapper fileMapper) {
//        this.fileMapper = fileMapper;
//    }

    public List<HelperVO> fileDataGet(String tempFileName, int sheetIndex) {
        ArrayList<String> telNumFail = new ArrayList<>();
        ArrayList<String> overName = new ArrayList<>();
        List<HelperVO> dataList = new ArrayList<>();
        FileDataVO fileDataVO = new FileDataVO();

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
                    row.getCell(TEL_CEL_NUMBER).setCellFormula(String.valueOf(row.getCell(TEL_CEL_NUMBER)));
                    String originalPhone = "0" + row.getCell(TEL_CEL_NUMBER);
                    String phoneNum = convertTelNo(originalPhone);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    if (phoneNum.equals("failed")){ telNumFail.add(originalPhone); }

                    HelperVO helperData = new HelperVO();
                    helperData.setHelperCi(row.getCell(0).getStringCellValue());
                    helperData.setHelperUserId((int) row.getCell(1).getNumericCellValue());
                    helperData.setHelperId((int) row.getCell(2).getNumericCellValue());
                    helperData.setHelperName(row.getCell(3).getStringCellValue());
                    helperData.setHelperMoblie(phoneNum);
                    String cal = simpleDateFormat.format(row.getCell(5).getDateCellValue());
                    helperData.setHelperDob(cal);
                    helperData.setHelperGender(row.getCell(6).getStringCellValue());
                    cal = simpleDateFormat.format(row.getCell(7).getDateCellValue());
                    if(!(row.getCell(8) == null)){
                        cal = simpleDateFormat.format(row.getCell(8).getDateCellValue());
                        helperData.setHelperWithdrewDatetime(cal);
                    }
                    helperData.setHelperIsServiceBlocked(row.getCell(9).getBooleanCellValue());
                    helperData.setHelperIsActive(row.getCell(10).getBooleanCellValue());

                    String overId = fileMapper.helperKeyCheck(helperData);
                    if(overId != null){ overName.add(overId); }

                    dataList.add(helperData);
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        try {
            if(!telNumFail.isEmpty()){
                fileDataVO.setConsequence("실패.. 전화번호 형식이 잘못 되었습니다." + telNumFail);
                fileDataVO.setOperationStatus("failed");
                fileDataVO.setTempFileName(tempFileName);
                fileMapper.excelDataUpdate(fileDataVO);
                dataList.clear();
                throw new Exception("전화번호 형식이 잘못 되었습니다.");
            }
            if (!overName.isEmpty()){
                fileDataVO.setConsequence("실패.. 중복 데이터가 있습니다." + overName);
                fileDataVO.setOperationStatus("failed");
                fileDataVO.setTempFileName(tempFileName);
                fileMapper.excelDataUpdate(fileDataVO);
                dataList.clear();
                throw new Exception("중복 데이터가 있습니다.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }

        private String convertTelNo(String mobTelNo) {
        if (mobTelNo != null) {
            // 일단 기존 - 전부 제거
            mobTelNo = mobTelNo.replaceAll(Pattern.quote("-"), "");
            if(mobTelNo.length() != 11){return "failed";}
        }
        return mobTelNo;
    }
}
