package com.example.fileupload.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    UserVO[] excelUpload(String fileName);

    void excelDataUpload(FileDataVO fileDataVO);

    void tempFileNameAdd(FileDataVO fileDataVO);

    String[] getWaitingTempFile();

    String getFileOriginalNameAndType(String fileName);

    void tempFileDelete();

    String rename(MultipartFile file, String fileName);

    void sucessFileDelete();

    SheetVO[] waitingFileSelect();
    void fileupload(SheetVO[] sheetVOS);


}
