package com.example.fileupload.polymorphism;

import com.example.fileupload.file.FileDataVO;
import com.example.fileupload.file.FileMapper;
import com.example.fileupload.file.UserVO;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserExcel implements FileParents {

    @Resource
    FileMapper fileMapper;


    public UserExcel(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }
    @Override
    public List<UserVO> fileDataGet(String tempFileName) {
//        ArrayList<String> telNumFail = new ArrayList<>();
        ArrayList<String> overName = new ArrayList<>();
        List<UserVO> dataList = new ArrayList<>();
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
                UserVO data = new UserVO();
                if(!(row.getCell(0) == null)){
                    data.setUserCi(row.getCell(0).getStringCellValue());
                }
                data.setUserId((int) row.getCell(1).getNumericCellValue());
                if(!(row.getCell(2) == null)){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String cal = simpleDateFormat.format(row.getCell(2).getDateCellValue());
                    data.setUserWithdrewDatetime(cal);
                }
                data.setUserIsServiceBlocked(row.getCell(3).getBooleanCellValue());
                data.setUserIsActive(row.getCell(4).getBooleanCellValue());

                String overId = fileMapper.userKeyCheck(data);
                if(overId != null){ overName.add(overId); }

                dataList.add(data);
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
                dataList.clear();
                throw new Exception("중복 데이터가 있습니다.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }


}
