package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class FileDTO {
   private Integer userPhoneNum;
   private String userName;
   private String userEmail;
}
