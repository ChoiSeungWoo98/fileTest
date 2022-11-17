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
    FileMapper fileMapper;
        @Override
        public void excelUpload(List<FileDTO> fileDTOS){
            fileMapper.excelUpload(fileDTOS);
        }
}
