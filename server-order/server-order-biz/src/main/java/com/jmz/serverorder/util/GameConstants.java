package com.jmz.serverorder.util;

/**
 * 游戏相关常量
 */
public class GameConstants {

    /**
     * 游戏状态
     */
    public static class Status {
        /**
         * 禁用
         */
        public static final Integer DISABLED = 0;
        
        /**
         * 启用
         */
        public static final Integer ENABLED = 1;
    }

    /**
     * 默认值
     */
    public static class Default {
        /**
         * 默认状态
         */
        public static final Integer STATUS = 1;
        
        /**
         * 默认排序
         */
        public static final Integer SORT_ORDER = 0;
        
        /**
         * 默认分页大小
         */
        public static final Integer PAGE_SIZE = 10;
        
        /**
         * 默认页码
         */
        public static final Integer PAGE_NUM = 1;
    }

    /**
     * 错误消息
     */
    public static class ErrorMessage {
        /**
         * 游戏名称不能为空
         */
        public static final String GAME_NAME_EMPTY = "游戏名称不能为空";
        
        /**
         * 游戏名称已存在
         */
        public static final String GAME_NAME_EXISTS = "游戏名称已存在";
        
        /**
         * 游戏不存在
         */
        public static final String GAME_NOT_FOUND = "游戏不存在";
        
        /**
         * 游戏ID不能为空
         */
        public static final String GAME_ID_EMPTY = "游戏ID不能为空";
        
        /**
         * 状态值无效
         */
        public static final String INVALID_STATUS = "状态值无效，只能是0或1";
    }
} 