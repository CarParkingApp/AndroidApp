/**
 * Created by Juliet on 19-Jun-18.
 */


package com.example.stek3.carparking.Processors;

public class Response {

    private int Code;
    private  String Message;

    public String getMessage() {
        return Message;
    }

    public int getCode() {
        return Code;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setCode(int code) {
        Code = code;
    }
}
