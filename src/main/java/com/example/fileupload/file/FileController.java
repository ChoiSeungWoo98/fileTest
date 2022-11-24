package com.example.fileupload.file;

import com.example.fileupload.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@RequestMapping("/excel/*")
@Slf4j
public class FileController {
    private final FileService fileService;
    private final Scheduler scheduler;
    @PostMapping("/read")
    private String readExcel(@RequestParam("file") MultipartFile file, Model model)
            throws IOException {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = simpleDateFormat.format(now);
        String fileName = file.getOriginalFilename();

//        파일의 데이터
        FileDataVO fileDataVO = new FileDataVO();
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        fileDataVO.setFileName(fileName);
        fileDataVO.setFileType(fileType);
        fileDataVO.setFileCreatedAt(nowTime);
        fileDataVO.setOperationStatus("waiting");

//        확장자를 가져와 엑셀 파일인지 구분하기 위해 사용
        String extension = FilenameUtils.getExtension(fileName);
        try {
            if (!extension.equals("xlsx") && !extension.equals("xls")) {
                fileDataVO.setConsequence("실패.. 엑셀파일만 업로드 해주세요.");
                fileDataVO.setOperationStatus("failed");
                fileService.excelDataUpload(fileDataVO);
                throw new Exception("엑셀파일만 업로드 해주세요.");
            }
        }catch (DuplicateKeyException e){
            // ignore
        }catch (Exception e){
            e.printStackTrace();
        }
        if(fileDataVO.getOperationStatus().equals("waiting")){
            fileService.excelDataUpload(fileDataVO);
        }
//        하나의 엑셀파일을 담는 용도
        Workbook workbook = extension.equals("xlsx")
                ? new XSSFWorkbook(file.getInputStream())
                : new HSSFWorkbook(file.getInputStream());

//        여기서 파일 변환 후 업데이트하자
        String newFileName = ((int) Math.floor(Math.random() * 100000 + 1)) + fileName;
        Path uploadedFilePath = scheduler.rename(file, newFileName);
        fileDataVO.setTempFileName(newFileName);
        fileDataVO.setTempFileNameOrigin(fileName);

//        임시파일 정보 추가
        fileService.tempFileNameAdd(fileDataVO);

//        model.addAttribute("datas", fileVO);

        return "excelList";

    }



    @GetMapping("/main")
    private String goMain(){
        return "excel";
    }


}


