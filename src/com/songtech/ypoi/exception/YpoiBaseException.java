package com.songtech.ypoi.exception;

/**
 * ypoi自定义基础错误类
 * Create By YINN on 2018/1/3
 */
public class YpoiBaseException extends Exception {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //***/


    public YpoiBaseException(String code) {
        this.code = code;
    }

    public YpoiBaseException(String message, String code) {
        super(message);
        this.code = code;
    }

    public YpoiBaseException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public YpoiBaseException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    public YpoiBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
