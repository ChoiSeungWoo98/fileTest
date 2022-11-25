package com.example.fileupload.polymorphism;

import com.example.fileupload.file.FileVO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Csv implements FileParents {
    @Override
    public List<FileVO> fileDataGet(String fileName) {
        //반환용 리스트
        List<FileVO> dataList = new ArrayList<>();

        BufferedReader br = null;

        try{
            br = Files.newBufferedReader(Paths.get(DATA_DIRECTORY+ File.separator+fileName));
            //Charset.forName("UTF-8");
            String line = "";

            while((line = br.readLine()) != null){
                //CSV 1행을 저장하는 리스트
                FileVO data = new FileVO();
                String array[] = line.split(",");
                //배열에서 리스트 반환
                data.setPhoneNum(array[0]);
                data.setName(array[1]);
                data.setEmail(array[2]);

                dataList.add(data);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        } catch (Exception e) {
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

        return dataList;
    }

    private String convertTelNo(String mobTelNo) {
        if (mobTelNo != null) {
            // 일단 기존 - 전부 제거
            mobTelNo = mobTelNo.replaceAll(Pattern.quote("-"), "");
            if(mobTelNo.length() != 11 && mobTelNo.length() != 8 && mobTelNo.length() != 10 && mobTelNo.length() != 9){return "failed";}
        }
        return mobTelNo;
    }



}
