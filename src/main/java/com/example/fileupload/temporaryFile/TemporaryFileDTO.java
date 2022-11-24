package com.example.fileupload.temporaryFile;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class TemporaryFileDTO {
    private String tempFileName;
    private Integer tempFileno;
}
