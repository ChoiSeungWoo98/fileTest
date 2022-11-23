package com.example.fileupload.file;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public interface FileService {
    FileVO[] excelUpload(Workbook workbook, FileDataVO fileDataVO);

    void excelDataUpload(FileDataVO fileDataVO);

}
