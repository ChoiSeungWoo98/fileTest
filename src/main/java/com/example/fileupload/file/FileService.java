package com.example.fileupload.file;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FileService {
    public void excelUpload(List<FileVO> fileVOS);

    public void excelDataUpload(FileDataVO fileDataVO);

    public String pkKeyCheck(List<FileVO> fileVOS);
}
