package com.example.fileupload.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileDAO {
    private final FileMapper fileMapper;

    public void excelUpload(List<FileVO> fileVOS){ fileMapper.excelUpload(fileVOS);}
    public void excelDataUpload(FileDataVO fileDataVO){ fileMapper.excelDataUpload(fileDataVO);}
    public String pkKeyCheck(FileVO fileVO){ return fileMapper.pkKeyCheck(fileVO); }
}
