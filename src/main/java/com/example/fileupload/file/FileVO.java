package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class FileVO {
   private String excelfilePhoneNum;
   private String excelfileName;
   private String excelfileEmail;
}
