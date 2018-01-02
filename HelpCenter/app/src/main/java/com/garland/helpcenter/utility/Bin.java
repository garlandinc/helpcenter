package com.garland.helpcenter.utility;

/**
 * Created by lemon on 10/29/2017.
 */

public class Bin  {
    private int code;
    private String title;
    private String message;


    public Bin(int code, String title, String message) {
        this.code = code;
        this.title = title;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

}
