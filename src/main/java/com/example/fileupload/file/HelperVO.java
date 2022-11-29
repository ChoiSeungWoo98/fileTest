package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class HelperVO {
   private int helperId;
   private int helperUserId;
   private String helperCi;
   private String helperName;
   private String helperMoblie;
   private String helperDob;
   private String helperGender;
   private String helperAcceptedDatetime;
   private String helperWithdrewDatetime;
   private Boolean helperIsServiceBlocked;
   private Boolean helperIsActive;
}
