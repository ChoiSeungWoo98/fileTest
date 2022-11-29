package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class HelperAnymanInfoVO {
   private int helperAnymanHelperId;
   private String helperAnymanCi;
   private int helperAnymanUserId;
   private String helperAnymanUserName;
   private String helperAnymanProfilePhoto;
   private String helperAnymanLicenses;
   private String helperAnymanExperience;
}
