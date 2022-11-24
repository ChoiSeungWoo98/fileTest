package com.example.fileupload.file;

import com.example.fileupload.temporaryFile.TemporaryFileDTO;
import com.example.fileupload.temporaryFile.TemporaryFileVO;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FileService {
    FileVO[] excelUpload(Workbook workbook, FileDataVO fileDataVO);

    void excelDataUpload(FileDataVO fileDataVO);

    FileDataVO[] findForExcelData();

    void createdTempFile(List<TemporaryFileVO> temporaryFileVOList);

    String[] selectedTempFile();

    void excelDataConectedTemp(TemporaryFileDTO temporaryFileDTO);


}
