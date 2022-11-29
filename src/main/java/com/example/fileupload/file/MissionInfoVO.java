package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MissionInfoVO {
   private int missionUserId;
   private int missionId;
   private String missionUserCi;
   private String missionCreatedDatetime;
   private String missionSavedState;
   private String missionType;
   private String missionTemplate;
   private String missionContent;
   private String missionStopover;
   private String missionDestination;
   private String missionCompletedDatetime;
   private int missionBidId;
   private String missionHelperCi;
   private int missionHelperId;
   private int missionBidAmount;
   private int missionFee;
}
