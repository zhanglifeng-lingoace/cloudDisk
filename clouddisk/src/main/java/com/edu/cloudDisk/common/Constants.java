package com.edu.cloudDisk.common;

/**
 * 静态常量定义的集合
 *
 * @author zhanglf
 * @date 2022-02-14
 */
public class Constants {
    /**
     * 密钥分隔符
     */
    public static final String USER_PASSWORD_SPLIT = "\007";
    /**
     * 家长
     */
    public static final int USER_ROLE_PARENT = 1;
    /**
     * 学生
     */
    public static final int USER_ROLE_STUDENT = 2;
    /**
     * 老师
     */
    public static final int USER_ROLE_TUTOR = 3;
    /**
     * 城市合伙人
     */
    public static final int USER_ROLE_AMBASSADOR = 4;

    /**
     * 未激活
     */
    public static final int USER_STATUS_INVALID = 0;
    /**
     * 已激活
     */
    public static final int USER_STATUS_VALID = 1;
    /**
     * 被封禁
     */
    public static final int USER_STATUS_FORBIDDEN = 2;
    /**
     * 被删除
     */
    public static final int USER_STATUS_DELETED = 3;

}
