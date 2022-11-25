package com.example.fileupload.file;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    FileVO[] excelUpload(Workbook workbook, String fileName);

    void excelDataUpload(FileDataVO fileDataVO);

    void tempFileNameAdd(FileDataVO fileDataVO);

    String[] getWaitingTempFile();

    String getFileOriginalNameAndType(String fileName);

    void tempFileDelete();

    String rename(MultipartFile file, String fileName);

    void sucessFileDelete();


}
