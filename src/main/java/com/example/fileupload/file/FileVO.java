package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class FileVO {
   private String phoneNum;
   private String name;
   private String email;
}
