package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class FileDataVO {
    private String fileName;
    private String fileType;
    private String fileCreatedAt;
    private String fileProcessedAt;
    private String operationStatus;
    private String consequence;
}
