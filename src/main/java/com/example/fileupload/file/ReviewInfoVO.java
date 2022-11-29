package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ReviewInfoVO {
   private int reviewHelperId;
   private String reviewHelperCi;
   private int reviewBidId;
   private String reviewMissionType;
   private String reviewMissionTemplate;
   private int reviewMannerScore;
   private int reviewPerfScore;
   private String reviewContent;
   private String reviewCreatedDatetime;
}
