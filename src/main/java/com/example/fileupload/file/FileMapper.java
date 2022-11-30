package com.example.fileupload.file;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    void excelUpload(List<UserVO> userVOS);
    void helperUpload(List<HelperVO> helperVOS);
    void helperAnymanUpload(List<HelperAnymanInfoVO> helperAnymanInfoVOS);
    void missionUpload(List<MissionInfoVO> missionInfoVOS);
    void reviewUpload(List<ReviewInfoVO> reviewInfoVOS);
    void excelDataUpdate(FileDataVO fileDataVO);
    String userKeyCheck(UserVO userVO);
    String helperKeyCheck(HelperVO helperVO);
    String helperAnymanKeyCheck(HelperAnymanInfoVO helperAnymanInfoVO);
    String reviewKeyCheck(ReviewInfoVO reviewInfoVO);
//    UserVO[] showUser(List<UserVO> userVOS);
    void tempFileNameAdd(FileDataVO fileDataVO);
    String[] getWaitingTempFile();
    void excelDataUpload(FileDataVO fileDataVO);
    String getFileOriginalNameAndType(String fileName);
    String[] getSucessFileDelete();
    void sheetUpload(SheetVO sheetVO);
    void sheetUpdate(SheetVO sheetVO);
    SheetVO[] waitingFileSelect();
}
