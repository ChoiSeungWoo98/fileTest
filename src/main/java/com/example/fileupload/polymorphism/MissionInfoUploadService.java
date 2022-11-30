package com.example.fileupload.polymorphism;

import com.example.fileupload.file.FileDataVO;
import com.example.fileupload.file.FileMapper;
import com.example.fileupload.file.HelperAnymanInfoVO;
import com.example.fileupload.file.MissionInfoVO;
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

@Service
@Slf4j
public class MissionInfoUploadService {
    @Resource
    FileMapper fileMapper;
    final String  DATA_DIRECTORY = "C:"+ File.separator+"Temp";

    public List<MissionInfoVO> fileDataGet(String tempFileName, int sheetIndex) {
        ArrayList<String> overName = new ArrayList<>();
        List<MissionInfoVO> dataList = new ArrayList<>();
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
                    MissionInfoVO missionInfoVo = new MissionInfoVO();
                    missionInfoVo.setMissionId((int) row.getCell(0).getNumericCellValue());
                    if(!(row.getCell(1) == null)){
                        missionInfoVo.setMissionUserCi(row.getCell(1).getStringCellValue());
                    }
                    missionInfoVo.setMissionUserId((int) row.getCell(2).getNumericCellValue());

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String cal = simpleDateFormat.format(row.getCell(3).getDateCellValue());

                    missionInfoVo.setMissionCreatedDatetime(cal);
                    missionInfoVo.setMissionSavedState(row.getCell(4).getStringCellValue());
                    missionInfoVo.setMissionType(row.getCell(5).getStringCellValue());
                    if(!(row.getCell(6) == null)){
                        missionInfoVo.setMissionTemplate(row.getCell(6).getStringCellValue());
                    }
                    missionInfoVo.setMissionContent(row.getCell(7).getStringCellValue());

                    if(!(row.getCell(8) == null)){
                        missionInfoVo.setMissionStopover(row.getCell(8).getStringCellValue());
                    }
                    if(!(row.getCell(9) == null)){
                        missionInfoVo.setMissionDestination(row.getCell(9).getStringCellValue());
                    }
                    if(!(row.getCell(10) == null)){
                        cal = simpleDateFormat.format(row.getCell(10).getDateCellValue());
                        missionInfoVo.setMissionCompletedDatetime(cal);
                    }

                    missionInfoVo.setMissionBidId((int) row.getCell(11).getNumericCellValue());
                    missionInfoVo.setMissionHelperCi(row.getCell(12).getStringCellValue());
                    missionInfoVo.setMissionHelperId((int) row.getCell(13).getNumericCellValue());
                    missionInfoVo.setMissionBidAmount((int) row.getCell(14).getNumericCellValue());
                    missionInfoVo.setMissionFee((int) row.getCell(15).getNumericCellValue());

                    dataList.add(missionInfoVo);
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
                dataList.clear();
                throw new Exception("중복 데이터가 있습니다.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }

}
