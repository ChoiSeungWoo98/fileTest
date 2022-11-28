package com.example.fileupload.file;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;

@Component
@Data
public class FileVO {
   private int id;
   private String ci;
   private String withdrewDatetime;
   private Boolean isServiceBlocked;
   private Boolean isActive;
}
