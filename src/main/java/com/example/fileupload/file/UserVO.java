package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserVO {
   private int userId;
   private String userCi;
   private String userWithdrewDatetime;
   private Boolean userIsServiceBlocked;
   private Boolean userIsActive;
}
