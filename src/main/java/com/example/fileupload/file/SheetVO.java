package com.example.fileupload.file;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class SheetVO{
    private String sheetType;
    private String sheetStatus;
    private String sheetFileName;
    private int sheetCount;
    private String sheetData;
}
