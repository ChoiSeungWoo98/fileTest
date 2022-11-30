package com.example.fileupload.file;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SheetVO {
    private String sheetType;
    private String sheetStatus;
    private String sheetFileName;
    private int sheetCount;
}
