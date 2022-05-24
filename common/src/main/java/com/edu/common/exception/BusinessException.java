package com.edu.common.exception;

/**
 * @author dusong
 */
public class BusinessException extends RuntimeException {
    private static final int DEFAULT_HTTP_CODE = 500;
    private static final long serialVersionUID = -1340328704145759576L;
    private final int code;
    private int msgLength;

    public BusinessException() {
        this.code = 500;
    }

    public BusinessException(int httpCode) {
        this.code = httpCode;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(String message, int httpCode) {
        super(message);
        this.code = httpCode;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    public BusinessException(String message, Throwable cause, int httpCode) {
        super(message, cause);
        this.code = httpCode;
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.code = 500;
    }

    public BusinessException(Throwable cause, int httpCode) {
        super(cause);
        this.code = httpCode;
    }

    public int getCode() {
        return this.code;
    }

    public int getMsgLength() {
        return this.msgLength;
    }

    public BusinessException setMsgLength(int msgLength) {
        this.msgLength = msgLength;
        return this;
    }
}