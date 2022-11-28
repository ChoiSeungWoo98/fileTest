package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class FileDataVO {
    private String companyName;
    private String fileName;
    private String fileType;
    private String fileDivision;
    private String fileCreatedAt;
    private String fileProcessedAt;
    private String fileSize;
    private int totaldataCount;
    private String uploader;
    private String operationStatus;
    private String consequence;
    private String tempFileName;
    private String tempFileNameOrigin;
}
