package com.example.fileupload.temporaryFile;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class TemporaryFileVO {
    private String tempFileName;
    private String tempFileStatus;
}
