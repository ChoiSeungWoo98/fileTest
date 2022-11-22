package com.example.fileupload.file;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    public void excelUpload(List<FileVO> fileVOS);
    public void excelDataUpload(FileDataVO fileDataVO);
    public String pkKeyCheck(FileVO fileVO);
}
