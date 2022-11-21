package com.example.fileupload.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("file")
@Primary
public class FileServiceImpl implements FileService {
    private final FileDAO fileDAO;

        @Override
        public void excelUpload(List<FileVO> fileVOS){ fileDAO.excelUpload(fileVOS); }

    @Override
    public void excelDataUpload(FileDataVO fileDataVO) { fileDAO.excelDataUpload(fileDataVO); }

    @Override
    public String pkKeyCheck(List<FileVO> fileVOS) { return fileDAO.pkKeyCheck(fileVOS); }
}
