package com.example.fileupload.polymorphism;

import com.example.fileupload.file.UserVO;

import java.io.File;
import java.util.List;

public interface FileParents {
    final int TEL_CEL_NUMBER = 4;
    final String  DATA_DIRECTORY = "C:"+ File.separator+"Temp";
    List<UserVO> fileDataGet(String fileName);
}

