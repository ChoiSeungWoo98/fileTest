package com.example.fileupload.polymorphism;

import com.example.fileupload.file.FileMapper;
import com.example.fileupload.file.FileVO;

import java.io.File;
import java.util.List;

public interface FileParents {
    final int TEL_CEL_NUMBER = 0;
    final String  DATA_DIRECTORY = "C:"+ File.separator+"Temp";
    List<FileVO> fileDataGet(String fileName);
}

