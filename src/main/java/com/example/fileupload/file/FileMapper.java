package com.example.fileupload.file;

import com.example.fileupload.temporaryFile.TemporaryFileDTO;
import com.example.fileupload.temporaryFile.TemporaryFileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    void excelUpload(List<FileVO> fileVOS);
    void excelDataUpload(FileDataVO fileDataVO);
    String pkKeyCheck(FileVO fileVO);
    FileVO[] showUser(List<FileVO> fileVOS);
    FileDataVO[] findForExcelData();
    void createdTempFile(List<TemporaryFileVO> temporaryFileVOList);
    String[] selectedTempFile();
    void excelDataConectedTemp(TemporaryFileDTO temporaryFileDTO);
}
