package com.example.fileupload.file;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    void excelUpload(List<FileVO> fileVOS);
    void excelDataUpdate(FileDataVO fileDataVO);
    String pkKeyCheck(FileVO fileVO);
    FileVO[] showUser(List<FileVO> fileVOS);
    void tempFileNameAdd(FileDataVO fileDataVO);
    String[] selectedTempFile();
    String[] getWaitingTempFile();
    void excelDataUpload(FileDataVO fileDataVO);
    String getFileOriginalNameAndType(String fileName);
}
