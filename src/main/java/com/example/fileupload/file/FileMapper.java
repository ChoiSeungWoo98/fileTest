package com.example.fileupload.file;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    public void excelUpload(List<FileDTO> fileDTOS);
}
