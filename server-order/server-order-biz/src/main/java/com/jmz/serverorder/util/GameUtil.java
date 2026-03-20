package com.jmz.serverorder.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 游戏相关工具类
 */
public class GameUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 格式化时间字符串
     *
     * @param dateTime 时间
     * @return 格式化后的字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(FORMATTER);
    }

    /**
     * 解析时间字符串
     *
     * @param dateTimeStr 时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("时间格式错误，正确格式：yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * 验证游戏状态
     *
     * @param status 状态值
     * @return 是否有效
     */
    public static boolean isValidStatus(Integer status) {
        return status != null && (status == 0 || status == 1);
    }

    /**
     * 获取状态描述
     *
     * @param status 状态值
     * @return 状态描述
     */
    public static String getStatusDescription(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "禁用";
            case 1:
                return "启用";
            default:
                return "未知";
        }
    }
} 