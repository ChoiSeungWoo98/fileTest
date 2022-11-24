package com.example.fileupload.file;

import com.example.fileupload.scheduler.Scheduler;
import com.example.fileupload.temporaryFile.TemporaryFileDTO;
import com.example.fileupload.temporaryFile.TemporaryFileVO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        fileDataVO.setFileName(fileName.substring(0,fileName.lastIndexOf(".")));
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

        // 디비에 저장되어 있는 임시파일 정보
        String[] tempFileNames = scheduler.temporaryFileGetName();
        scheduler.temporaryFileInsert(tempFileNames);
        tempFileNames = scheduler.temporaryFileGetName();

//        List<TemporaryFileDTO> tempNos = new ArrayList<>();
        for (String temp:tempFileNames) {
            TemporaryFileDTO temporaryFileDTO = new TemporaryFileDTO();
            temporaryFileDTO.setTempFileName(temp);
            temporaryFileDTO.setTempFileno(Integer.parseInt(temp.substring(temp.lastIndexOf("_")+1, temp.lastIndexOf(".")))+1);
            fileService.excelDataConectedTemp(temporaryFileDTO);
//            tempNos.add(temporaryFileDTO);
        }
//        fileService.excelDataConectedTemp(tempNos);


//        엑셀에서 받은 정보를 담은 list를 DB에 저장
//        FileVO fileVO[] = fileService.excelUpload(workbook, fileDataVO);
//        model.addAttribute("datas", fileVO);

        return "excelList";

    }



    @GetMapping("/main")
    private String goMain(){
        return "excel";
    }


}


