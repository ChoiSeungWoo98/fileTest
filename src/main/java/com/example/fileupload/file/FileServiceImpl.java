package com.example.fileupload.file;

import com.example.fileupload.polymorphism.Csv;
import com.example.fileupload.polymorphism.Excel;
import com.example.fileupload.polymorphism.FileParents;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("file")
@Primary
public class FileServiceImpl implements FileService {

    @Resource
    ApplicationContext context;

    private final FileMapper fileMapper;
    private final String  DATA_DIRECTORY = "C:\\Temp";

        @Override
        public FileVO[] excelUpload(String tempFileName){
            String fileType = FilenameUtils.getExtension(tempFileName);
            FileDataVO fileDataVO = new FileDataVO();
            boolean fileUploadSucessed = true;
            FileParents fileParents = null;


            if(fileType.equals("xlsx") || fileType.equals("xls")){
                fileParents = new Excel(context.getBean(FileMapper.class));
            }else if(fileType.equals("csv")) {
                fileParents = new Csv();
            }
            if (fileParents == null) {
                // 처리 불가능 타입
            }
            List<FileVO> dataList = fileParents.fileDataGet(tempFileName);




            fileDataVO.setTempFileName(tempFileName);

//            String extension = FilenameUtils.getExtension(file);
//            Workbook workbook = extension.equals("xlsx")
//                    ? new XSSFWorkbook(fileInputStream)
//                    : new HSSFWorkbook(fileInputStream);

//            workbook.getSheetAt(0).forEach( row -> {
//                try {
//                    row.getCell(TEL_CEL_NUMBER).setCellFormula(String.valueOf(row.getCell(TEL_CEL_NUMBER)));
//                    String originalPhone = "0" + row.getCell(TEL_CEL_NUMBER);
////                    String phoneNum = convertTelNo(originalPhone);
//
////                    if (phoneNum.equals("failed")){ telNumFail.add(originalPhone); }
//
//                    FileVO data = new FileVO();
////                    data.setPhoneNum(phoneNum);
//                    data.setName(row.getCell(1).getStringCellValue());
//                    data.setEmail(row.getCell(2).getStringCellValue());
//
//                    if(fileMapper.pkKeyCheck(data) != null){ overName.add(fileMapper.pkKeyCheck(data)); }
//
//                    dataList.add(data);
//                }catch (NullPointerException e){
//                    e.printStackTrace();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            });

//            중복전화번호랑 중복이름 확인해야함!!!!!
//            csv파일 업로드시 오류 있음

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
                    fileDataVO.setTotaldataCount(dataList.size());
                    fileMapper.excelDataUpdate(fileDataVO);
//                    File deleteFile = new File(DATA_DIRECTORY + File.separator + fileName);
//                    if(deleteFile.exists()){
////                        deleteFile.deleteOnExit();
//                        deleteFile.delete();
//                    }
                }
                if(dataList.size() != 0){
                    fileMapper.excelUpload(dataList);
                }
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
    @Override
    public String[] getWaitingTempFile() { return fileMapper.getWaitingTempFile(); }
    @Override
    public String getFileOriginalNameAndType(String fileName) { return fileMapper.getFileOriginalNameAndType(fileName); }





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
