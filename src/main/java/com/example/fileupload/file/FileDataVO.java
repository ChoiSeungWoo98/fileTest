package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class FileDataVO {
    private String excelfiledataName;
    private String excelfiledataType;
    private String excelfiledataUploadTime;
    private String excelfiledataProcessingTime;
    private String excelfiledataOperationStatus;
    private String excelfiledataResult;
}
