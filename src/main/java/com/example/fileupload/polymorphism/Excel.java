package com.example.fileupload.polymorphism;

import com.example.fileupload.file.FileDataVO;
import com.example.fileupload.file.FileMapper;
import com.example.fileupload.file.FileVO;
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
import java.util.regex.Pattern;

@Service
public class Excel implements FileParents {
    @Resource
    FileMapper fileMapper;


    public Excel(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public List<FileVO> fileDataGet(String tempFileName)  {
        ArrayList<String> telNumFail = new ArrayList<>();
        ArrayList<String> overName = new ArrayList<>();
        List<FileVO> dataList = new ArrayList<>();
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

        workbook.getSheetAt(0).forEach( row -> {
            try {
                row.getCell(TEL_CEL_NUMBER).setCellFormula(String.valueOf(row.getCell(TEL_CEL_NUMBER)));
                String originalPhone = "0" + row.getCell(TEL_CEL_NUMBER);
                String phoneNum = convertTelNo(originalPhone);

                if (phoneNum.equals("failed")){ telNumFail.add(originalPhone); }
                FileVO data = new FileVO();
                data.setPhoneNum(phoneNum);
                data.setName(row.getCell(1).getStringCellValue());
                data.setEmail(row.getCell(2).getStringCellValue());

                String falsePhone = fileMapper.pkKeyCheck(data);
                if(falsePhone != null){ overName.add(falsePhone); }

                dataList.add(data);
            }catch (NullPointerException e){
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
            if(mobTelNo.length() != 11 /*&& mobTelNo.length() != 8 && mobTelNo.length() != 10 && mobTelNo.length() != 9*/){return "failed";}
        }
        return mobTelNo;
    }



}
