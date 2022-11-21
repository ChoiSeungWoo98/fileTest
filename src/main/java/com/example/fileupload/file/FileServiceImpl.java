package com.example.fileupload.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("file")
public class FileServiceImpl implements FileService {
    private final FileDAO fileDAO;

        @Override
        public void excelUpload(List<FileVO> fileVOS){
            fileDAO.excelUpload(fileVOS);
        }
}
