package com.edu.cloudDisk.common;

/**
 * 枚举了一些常用API操作码
 */
public enum ResultCode implements IErrorCode {

    SUCCESS(200, "操作成功"),
    UPDATE_FAILD(501, "操作失败"),
    SEARCH_FAILD(502, "数据不存在"),
    PARAMS_FAILD(503, "请确认请求参数");

    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static ResultCode getResultCode(long code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.getCode() == code) {
                return resultCode;
            }
        }
        return null;
    }
}
