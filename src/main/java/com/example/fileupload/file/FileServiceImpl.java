package com.example.fileupload.file;

import com.example.fileupload.polymorphism.Csv;
import com.example.fileupload.polymorphism.Excel;
import com.example.fileupload.polymorphism.FileParents;
import lombok.RequiredArgsConstructor;
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
            FileParents fileParents = null;


            if(fileType.equals("xlsx") || fileType.equals("xls")){
                fileParents = new Excel(context.getBean(FileMapper.class));
            }else if(fileType.equals("csv")) {
                fileParents = new Csv(context.getBean(FileMapper.class));
            }
            if (fileParents == null) {
                // 처리 불가능 타입
            }
            List<FileVO> dataList = fileParents.fileDataGet(tempFileName);
            fileDataVO.setTempFileName(tempFileName);

            try {
                if(!dataList.isEmpty()){
                    Date now = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowTime = simpleDateFormat.format(now);
                    fileDataVO.setFileProcessedAt(nowTime);
                    fileDataVO.setOperationStatus("sucessed");
                    fileDataVO.setConsequence("성공");
                    fileDataVO.setTotaldataCount(dataList.size());
                    fileMapper.excelDataUpdate(fileDataVO);
                    fileMapper.excelUpload(dataList);
//                    File deleteFile = new File(DATA_DIRECTORY + File.separator + fileName);
//                    if(deleteFile.exists()){
////                        deleteFile.deleteOnExit();
//                        deleteFile.delete();
//                    }
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
