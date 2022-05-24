package com.edu.cloudDisk.core.common;

/**
 * @Description Snowflake主键id生成配置枚举 格式：PRIMARY_KEY+表名，大写
 * @Author zhanglifeng
 */
public enum PrimaryKey {
    PRIMARY_KEY_COURSE_PACKAGE_TREE(1, 1),
    PRIMARY_KEY_RANDOM(0, 0);


    private final long workerId;
    private final long datacenterId;

    PrimaryKey(long workerId, long datacenterId) {
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }
}