package com.example.fileupload.polymorphism;

import com.example.fileupload.file.FileDataVO;
import com.example.fileupload.file.FileMapper;
import com.example.fileupload.file.FileVO;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
@Slf4j
public class Csv implements FileParents {
    @Resource
    FileMapper fileMapper;


    public Csv(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public List<FileVO> fileDataGet(String tempFileName) {
        //반환용 리스트
        List<FileVO> dataList = new ArrayList<>();
        ArrayList<String> telNumFail = new ArrayList<>();
        ArrayList<String> overName = new ArrayList<>();
        FileDataVO fileDataVO = new FileDataVO();

        File csv = new File(DATA_DIRECTORY+File.separator+tempFileName);
        BufferedReader br = null;
        String line = "";

        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "UTF-8"));
            while ((line = br.readLine()) != null){
                String[] lineArr = line.split(",");

                String phoneNum = convertTelNo(lineArr[0]);
                if (phoneNum.equals("failed")){ telNumFail.add(phoneNum); }

                FileVO fileVO = new FileVO();
                fileVO.setPhoneNum(phoneNum);
                fileVO.setName(lineArr[1]);
                fileVO.setEmail(lineArr[2]);
                log.info("----------------------여기가 csv---------------------");
                log.info(lineArr[0]);
                log.info(phoneNum+" | "+lineArr[1]+" | "+lineArr[2]);
                log.info("---------------------------------------------");
                dataList.add(fileVO);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        } finally{
            try{
                if(br != null){
                    br.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        try {
            if(!telNumFail.isEmpty()){
                fileDataVO.setConsequence("실패.. 전화번호 형식이 잘못 되었습니다." + telNumFail);
                fileDataVO.setOperationStatus("failed");
                fileDataVO.setTempFileName(tempFileName);
                fileMapper.excelDataUpdate(fileDataVO);
                dataList.clear();
                throw new Exception("전화번호 형식이 잘못 되었습니다.");
            }
            if (!overName.isEmpty()){
                fileDataVO.setConsequence("실패.. 중복 데이터가 있습니다." + overName);
                fileDataVO.setOperationStatus("failed");
                fileDataVO.setTempFileName(tempFileName);
                fileMapper.excelDataUpdate(fileDataVO);
                dataList.clear();
                throw new Exception("중복 데이터가 있습니다.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return dataList;
    }

    private String convertTelNo(String mobTelNo) {
        if (mobTelNo != null) {
            // 일단 기존 - 전부 제거
            mobTelNo = mobTelNo.replaceAll(Pattern.quote("-"), "");
            if(mobTelNo.length() != 11 /*&& mobTelNo.length() != 8 && mobTelNo.length() != 10 && mobTelNo.length() != 9*/){return "failed";}
        }
        return mobTelNo;
    }



}
