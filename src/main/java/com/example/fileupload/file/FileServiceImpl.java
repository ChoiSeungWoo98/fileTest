package com.example.fileupload.file;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Qualifier("file")
@Primary
public class FileServiceImpl implements FileService {
    private final FileMapper fileMapper;
    final int TEL_CEL_NUMBER = 0;

        @Override
        public FileVO[] excelUpload(Workbook workbook, String fileName){
            List<FileVO> dataList = new ArrayList<>();
            ArrayList<String> overName = new ArrayList<>();
            ArrayList<String> telNumFail = new ArrayList<>();
            FileDataVO fileDataVO = new FileDataVO();
            boolean fileUploadSucessed = true;

            fileDataVO.setTempFileName(fileName);

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

                    if(fileMapper.pkKeyCheck(data) != null){ overName.add(fileMapper.pkKeyCheck(data)); }

                    dataList.add(data);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            try {
                if(!telNumFail.isEmpty()){
                    fileUploadSucessed = false;
                    fileDataVO.setConsequence("실패.. 전화번호 형식이 잘못 되었습니다." + telNumFail);
                    fileDataVO.setOperationStatus("failed");
                    fileMapper.excelDataUpdate(fileDataVO);
                    throw new Exception("전화번호 형식이 잘못 되었습니다.");
                }
                if (!overName.isEmpty()){
                    fileUploadSucessed = false;
                    fileDataVO.setConsequence("실패.. 중복 데이터가 있습니다." + overName);
                    fileDataVO.setOperationStatus("failed");
                    fileMapper.excelDataUpdate(fileDataVO);
                    throw new Exception("중복 데이터가 있습니다.");
                }
                if(fileUploadSucessed){
                    Date now = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowTime = simpleDateFormat.format(now);
                    fileDataVO.setFileProcessedAt(nowTime);
                    fileDataVO.setOperationStatus("sucessed");
                    fileDataVO.setConsequence("성공");
                    fileMapper.excelDataUpdate(fileDataVO);
                }
                fileMapper.excelUpload(dataList);
            } catch (DuplicateKeyException e) {
                // ignore
            } catch (Exception e){
                e.printStackTrace();
            }

            return fileMapper.showUser(dataList);
        }

    @Override
    public void excelDataUpload(FileDataVO fileDataVO) { fileMapper.excelDataUpload(fileDataVO); }

    @Override
    public void tempFileNameAdd(FileDataVO fileDataVO) { fileMapper.tempFileNameAdd(fileDataVO); }
    public String[] selectedTempFile() { return fileMapper.selectedTempFile(); }

    @Override
    public String[] getWaitingTempFile() { return fileMapper.getWaitingTempFile(); }

    @Override
    public String getFileOriginalNameAndType(String fileName) { return fileMapper.getFileOriginalNameAndType(fileName); }


    private String convertTelNo(String mobTelNo) {
        if (mobTelNo != null) {
            // 일단 기존 - 전부 제거
            mobTelNo = mobTelNo.replaceAll(Pattern.quote("-"), "");
            if(mobTelNo.length() != 11 && mobTelNo.length() != 8 && mobTelNo.length() != 10 && mobTelNo.length() != 9){return "failed";}
        }
        return mobTelNo;
    }
}
