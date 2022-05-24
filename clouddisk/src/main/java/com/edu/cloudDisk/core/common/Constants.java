package com.edu.cloudDisk.core.common;

/**
 * @author lingoace
 * @title: Constants
 * @date 2021/12/29下午5:12
 */
public class Constants {
    /**
     * 课程树 状态：0.草稿、1.发布、2.归档
     */
    public static final Integer COURSE_INFO_STATUS_DRAFT = 0;
    /**
     * 课程树 状态：0.草稿、1.发布、2.归档
     */
    public static final Integer COURSE_INFO_STATUS_RELEASED = 1;
    /**
     * 课程树 状态：0.草稿、1.发布、2.归档
     */
    public static final Integer COURSE_INFO_STATUS_ARCHIVE = 2;

    /**
     * 无效
     */
    public static final Integer STATUS_INVALID = 0;
    /**
     * 有效
     */
    public static final Integer STATUS_VALID = 1;

    /**
     * 归档
     */
    public static final Integer STATUS_ARCHIVE = 2;

    public static final String KNOWLEAGE_KEYWORD = "languagePoints";

    /**
     * 试听课包
     */
    public static final Integer PACKAGE_TYPE_CODE_IS_TRAIL = 0;

    /**
     * 正式课包
     */
    public static final Integer PACKAGE_TYPE_CODE_IS_SYSTEM = 1;
}
