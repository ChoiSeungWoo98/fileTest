package com.example.fileupload.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@RequestMapping("/excel/*")
@Slf4j
public class FileController {
    enum fileDivisions{애니맨_회원, 헬퍼_전환, 헬퍼_정보, 미션_정보, 피드백_정보}
//    enum fileDivisions{ANYMAN_MEMBER, HELPER_CONVERSION, HELPER_INFORMATION, MISSION_INFORMATION, FEEDBACK_INFORMATION}
    private final FileService fileService;
    @PostMapping("/read")
    private String readExcel(/*@RequestParam("file") MultipartFile file*/MultipartHttpServletRequest mRequest) {
        List<MultipartFile> files = mRequest.getFiles("file");
        files.forEach(file -> {
            Date now = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = simpleDateFormat.format(now);
            String fileName = file.getOriginalFilename();

            FileDataVO fileDataVO = new FileDataVO();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            
//        이거는 파일을 구분하기 위함(임의로 랜덤으로 뽑게 만듦 추후에 바꿔야함)
            Random r = new Random();
            int ran = r.nextInt(4);
            fileDivisions fileDivision;
            switch (ran){case 0: fileDivision = fileDivisions.애니맨_회원; break; case 1: fileDivision = fileDivisions.헬퍼_전환; break; case 2: fileDivision = fileDivisions.헬퍼_정보; break; case 3: fileDivision = fileDivisions.미션_정보; break; case 4: fileDivision = fileDivisions.피드백_정보; break; default: fileDivision = null;}
            fileDataVO.setFileDivision(String.valueOf(fileDivision));


            fileDataVO.setFileName(fileName);
            fileDataVO.setFileType(fileType);
            fileDataVO.setFileCreatedAt(nowTime);
            fileDataVO.setOperationStatus("waiting");

//        로그인한 정보에 존재할 거 같음. (쿠키나 세션에서 가져와야함 추후에 바꿔야함)
            fileDataVO.setCompanyName("AnyMan");
            fileDataVO.setUploader("김애니");

            if(file.getSize()/(1024*1024) >= 1){
                fileDataVO.setFileSize(Math.ceil(file.getSize()/(1024.0*1024.0)*10.0)/10.0 + "MB");
            } else if (file.getSize()/1024 >= 1) {
                fileDataVO.setFileSize(Math.ceil(file.getSize()/(1024.0)) + "KB");
            } else {
                fileDataVO.setFileSize(Math.ceil(file.getSize())+"BYTE");
            }


//        확장자를 가져와 파일을 구분하기 위해 사용
            String extension = FilenameUtils.getExtension(fileName);
            try {
                if (extension.equals("xlsx") || extension.equals("xls") || extension.equals("csv")) {
                    //        여기서 파일 변환 후 업데이트하자
                    String newFileName = ((int) Math.floor(Math.random() * 100000 + 1)) + fileName;
                    String uploadName = fileService.rename(file, newFileName);

                    fileDataVO.setTempFileName(uploadName);
                    fileDataVO.setTempFileNameOrigin(fileName);
                    fileService.excelDataUpload(fileDataVO);
                } else {
                    fileDataVO.setConsequence("실패.. 엑셀파일 및 csv파일을 업로드 해주세요.");
                    fileDataVO.setOperationStatus("failed");
                    fileService.excelDataUpload(fileDataVO);
                    throw new Exception("엑셀파일 및 csv파일을 업로드 해주세요.");
                }
            }catch (DuplicateKeyException e){
                // ignore
            }catch (Exception e){
                e.printStackTrace();
            }




//        임시파일 정보 추가
//            fileService.tempFileNameAdd(fileDataVO);
        });


//        model.addAttribute("datas", fileVO);
        return "excelList";

    }



    @GetMapping("/main")
    private String goMain(){
        return "excel";
    }


}


