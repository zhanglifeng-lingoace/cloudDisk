package com.edu.cloudDisk.core.utils;


import com.edu.cloudDisk.core.common.PrimaryKey;

/**
 * @author zhanglifeng
 */
public class SnowflakeUtil {
    private static final long twepoch = 1420041600000L;
    private static final long workerIdBits = 5L;
    private static final long datacenterIdBits = 5L;
    private static final long maxWorkerId = 31L;
    private static final long maxDatacenterId = 31L;
    private static final long sequenceBits = 12L;
    private static final long workerIdShift = 12L;
    private static final long datacenterIdShift = 17L;
    private static final long timestampLeftShift = 22L;
    private static final long sequenceMask = 4095L;
    private long workerId;
    private long datacenterId;
    private static long sequence = 0L;
    private static long lastTimestamp = -1L;

    private SnowflakeUtil(long workerId, long datacenterId) {
        if (workerId <= 31L && workerId >= 0L) {
            if (datacenterId <= 31L && datacenterId >= 0L) {
                this.workerId = workerId;
                this.datacenterId = datacenterId;
            } else {
                throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", 31L));
            }
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", 31L));
        }
    }

    private synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            //系统回拨后，lastTimestamp为正常的大的时间，而timestamp变成最新的回拨后的小的时间。为保证id不变，可以将
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        } else {
            if (lastTimestamp == timestamp) {
                sequence = sequence + 1L & 4095L;
                if (sequence == 0L) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }

            lastTimestamp = timestamp;
            return timestamp - 1420041600000L << 22 | this.datacenterId << 17 | this.workerId << 12 | sequence;
        }
    }

    private static long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for (timestamp = timeGen(); timestamp <= lastTimestamp; timestamp = timeGen()) {
        }

        return timestamp;
    }

    private static long timeGen() {
        return System.currentTimeMillis();
    }

    public static String getPrimaryKey(PrimaryKey primaryKey) {
        SnowflakeUtil idWorker = new SnowflakeUtil(primaryKey.getWorkerId(), primaryKey.getDatacenterId());
        return String.valueOf(idWorker.nextId());
    }

    public static long getPrimaryKeyForLong(PrimaryKey primaryKey) {
        SnowflakeUtil idWorker = new SnowflakeUtil(primaryKey.getWorkerId(), primaryKey.getDatacenterId());
        return idWorker.nextId();
    }
}
