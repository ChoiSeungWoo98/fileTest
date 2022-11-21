package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class FileMetaDataVO {
    private String excelfiledataType;
    private int excelfiledataTotal;
    private String excelfiledataUploadTime;
    private String excelfiledataResult;
}
