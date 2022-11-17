package com.example.fileupload.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileDAO {
    private final FileMapper fileMapper;

    public void excelUpload(List<FileDTO> fileDTOS){ fileMapper.excelUpload(fileDTOS);}
}
