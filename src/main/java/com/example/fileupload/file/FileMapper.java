package com.example.fileupload.file;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    void excelUpload(List<UserVO> UserVOS);
    void excelDataUpdate(FileDataVO fileDataVO);
    String userKeyCheck(UserVO userVO);
    String helperKeyCheck(HelperVO helperVO);
    UserVO[] showUser(List<UserVO> userVOS);
    void tempFileNameAdd(FileDataVO fileDataVO);
    String[] getWaitingTempFile();
    void excelDataUpload(FileDataVO fileDataVO);
    String getFileOriginalNameAndType(String fileName);
    String[] getSucessFileDelete();
}
