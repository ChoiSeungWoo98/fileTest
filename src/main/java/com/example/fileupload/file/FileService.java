package com.example.fileupload.file;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public interface FileService {
    FileVO[] excelUpload(Workbook workbook, String fileName);

    void excelDataUpload(FileDataVO fileDataVO);

    void tempFileNameAdd(FileDataVO fileDataVO);

    String[] selectedTempFile();

    String[] getWaitingTempFile();

    String getFileOriginalNameAndType(String fileName);

}
