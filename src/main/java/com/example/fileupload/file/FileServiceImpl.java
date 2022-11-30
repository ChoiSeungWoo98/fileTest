package com.example.fileupload.file;

import com.example.fileupload.polymorphism.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("file")
@Primary
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    CsvUploader csvUploader;
    @Resource
    UserUploadInternalService userUploadService;
    @Resource
    HelperPersonalInfoUploadInternalService helperPersonalInfoUploadService;
    @Resource
    HelperAnymanInfoUploadService helperAnymanInfoUploadService;
    @Resource
    MissionInfoUploadService missionInfoUploadService;
    @Resource
    ReviewInfoUploadService reviewInfoUploadService;
    private final FileMapper fileMapper;
    private final String  DATA_DIRECTORY = "C:\\Temp";

        @Override
        public UserVO[] excelUpload(String tempFileName){
            String fileType = FilenameUtils.getExtension(tempFileName);
//            FileDataVO fileDataVO = new FileDataVO();

            boolean fileCheck = true;
            if(fileType.equals("xlsx") || fileType.equals("xls")){
                fileCheck = false;
//                String extension = FilenameUtils.getExtension(tempFileName);
                Workbook workbook = null;
                try {
                    FileInputStream fileInputStream = new FileInputStream(DATA_DIRECTORY + File.separator + tempFileName);
                    workbook = fileType.equals("xlsx")
                            ? new XSSFWorkbook(fileInputStream)
                            : new HSSFWorkbook(fileInputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                workbook.getAllNames().forEach(name -> {
                    String sheetName = name.getSheetName();
                    SheetVO sheetVO = new SheetVO();
                    sheetVO.setSheetType(sheetName);
                    sheetVO.setSheetFileName(tempFileName.substring(5));

                    switch (sheetName){
                        case "회원 정보":
                            List<UserVO> userList = userUploadService.fileDataGet(tempFileName, name.getSheetIndex());
                            sheetVO.setSheetCount(userList.size());
                            sucessedFileUpdate(userList, tempFileName, sheetName);
                            fileMapper.excelUpload(userList);
                            break;
                        case "헬퍼 개인정보":
                            List<HelperVO> helperList = helperPersonalInfoUploadService.fileDataGet(tempFileName, name.getSheetIndex());
                            sheetVO.setSheetCount(helperList.size());
                            sucessedFileUpdate(helperList, tempFileName, sheetName);
                            fileMapper.helperUpload(helperList);
                            break;
                        case "헬퍼 애니맨정보":
                            List<HelperAnymanInfoVO> helperAnymanList = helperAnymanInfoUploadService.fileDataGet(tempFileName, name.getSheetIndex());
                            sheetVO.setSheetCount(helperAnymanList.size());
                            sucessedFileUpdate(helperAnymanList, tempFileName, sheetName);
                            fileMapper.helperAnymanUpload(helperAnymanList);
                            break;
                        case "미션 정보":
                            List<MissionInfoVO> missionInfoVOList = missionInfoUploadService.fileDataGet(tempFileName, name.getSheetIndex());
                            sheetVO.setSheetCount(missionInfoVOList.size());
                            sucessedFileUpdate(missionInfoVOList, tempFileName, sheetName);
                            fileMapper.missionUpload(missionInfoVOList);
                            break;
                        case "리뷰 정보":
                            List<ReviewInfoVO> reviewInfoVOList = reviewInfoUploadService.fileDataGet(tempFileName, name.getSheetIndex());
                            sheetVO.setSheetCount(reviewInfoVOList.size());
                            sucessedFileUpdate(reviewInfoVOList, tempFileName, sheetName);
                            fileMapper.reviewUpload(reviewInfoVOList);
                            break;
                        default:
                    }
                    if(sheetVO.getSheetCount() != 0){
                        fileMapper.sheetUpload(sheetVO);
                    }
                });
//                fileParents = new Excel(context.getBean(FileMapper.class));
            }else if(fileType.equals("csv")) {
                fileCheck = false;
                FileParents fileParents = null;
            }
            if (fileCheck) {
                // 처리 불가능 타입
            }

            return /*fileMapper.showUser(dataList)*/null;
        }
    @Override
    public void excelDataUpload(FileDataVO fileDataVO) { fileMapper.excelDataUpload(fileDataVO); }
    @Override
    public void tempFileNameAdd(FileDataVO fileDataVO) { fileMapper.tempFileNameAdd(fileDataVO); }
    @Override
    public String[] getWaitingTempFile() { return fileMapper.getWaitingTempFile(); }
    @Override
    public String getFileOriginalNameAndType(String fileName) { return fileMapper.getFileOriginalNameAndType(fileName); }

    private <T> void sucessedFileUpdate(List<T> dataList, String tempFileName, String division) {
        FileDataVO fileDataVO = new FileDataVO();
        fileDataVO.setTempFileName(tempFileName);
        try {
            Date now = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = simpleDateFormat.format(now);
            fileDataVO.setFileProcessedAt(nowTime);
            fileDataVO.setFileDivision(division);
            fileDataVO.setOperationStatus("sucessed");
            fileDataVO.setConsequence("성공");
            fileDataVO.setTotaldataCount(fileDataVO.getTotaldataCount() + dataList.size());
            fileMapper.excelDataUpdate(fileDataVO);

//                    File deleteFile = new File(DATA_DIRECTORY + File.separator + fileName);
//                    if(deleteFile.exists()){
////                        deleteFile.deleteOnExit();
//                        deleteFile.delete();
//                    }
        } catch (DuplicateKeyException e) {
            // ignore
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public File[] allFileGet(String type){
        File dir = new File(DATA_DIRECTORY);
        FileFilter fileType = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(type);
            }
        };
        File[] filesType = dir.listFiles(fileType);
        return filesType;
    }

    public void tempFileDelete(){
        String type = "tmp";
        File[] tempNames = allFileGet(type);
        for(File tempFile : tempNames){
            if(tempFile.exists()){
                tempFile.delete();
            }
        }
    }

    public String rename(MultipartFile file, String fileName) {
        String fileUrl = DATA_DIRECTORY + File.separator + fileName;
        Path newFile = Paths.get(fileUrl);
        try {
            Files.copy(file.getInputStream(), newFile, StandardCopyOption.REPLACE_EXISTING);
//            return newFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            e.printStackTrace();
        }
        return fileName;
    }

    public void sucessFileDelete() {
        String[] sucessFiles = fileMapper.getSucessFileDelete();
        for (String sucessFile : sucessFiles) {
            File file = new File(DATA_DIRECTORY+ File.separator + sucessFile);
            file.delete();
        }
    }
}
