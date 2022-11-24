package com.example.fileupload.exception;

// 추후 처리
public class MyException extends RuntimeException{

    public MyException(){}

    MyException(String msg){
        super(msg);
    }

}
