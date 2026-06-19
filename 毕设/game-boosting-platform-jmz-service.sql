/*
 Navicat Premium Data Transfer

 Source Server         : j
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : game-boosting-platform-jmz-service

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 12/04/2026 18:31:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_jmz_game_servers
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_game_servers`;
CREATE TABLE `tb_jmz_game_servers`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `game_id` int NOT NULL COMMENT '所属游戏ID',
  `system_id` int NOT NULL COMMENT '所属系统ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '区服名称',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_name`(`name` ASC) USING BTREE,
  INDEX `idx_game`(`game_id` ASC) USING BTREE,
  INDEX `idx_system`(`system_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2081 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_game_systems
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_game_systems`;
CREATE TABLE `tb_jmz_game_systems`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `game_id` int NOT NULL COMMENT '游戏ID',
  `system_id` int NOT NULL COMMENT '系统ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_game_system`(`game_id` ASC, `system_id` ASC) USING BTREE,
  INDEX `idx_game`(`game_id` ASC) USING BTREE,
  INDEX `idx_system`(`system_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_games
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_games`;
CREATE TABLE `tb_jmz_games`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '游戏名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '游戏图标',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_menu`;
CREATE TABLE `tb_jmz_menu`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称（如首页、用户管理）',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单路径（如 /home、/users）',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径（如 @/views/home/Index.vue），目录可为空',
  `parent_id` int UNSIGNED NULL DEFAULT NULL COMMENT '父菜单ID（根目录为 NULL）',
  `type` tinyint NOT NULL DEFAULT 0 COMMENT '类型：0=目录，1=菜单项',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标（如 dashboard、user）',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '重定向路径（如 noRedirect）',
  `order_num` int NOT NULL DEFAULT 0 COMMENT '排序字段',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识（如 system:user:list）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `hidden` int NULL DEFAULT 0 COMMENT '是否隐藏：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tb_jmz_menu_ibfk_1`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `tb_jmz_menu_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `tb_jmz_menu` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_messages
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_messages`;
CREATE TABLE `tb_jmz_messages`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联订单ID（如为订单相关消息，否则可为空）',
  `sender_id` bigint NOT NULL COMMENT '发送者用户ID',
  `receiver_id` bigint NULL DEFAULT NULL COMMENT '接收者用户ID（如为系统消息可为空）',
  `sender_type` tinyint NULL DEFAULT 1 COMMENT '发送者类型：1-用户，2-系统，3-客服',
  `message_type` tinyint NULL DEFAULT 1 COMMENT '消息类型：1-文本，2-图片，3-文件',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件/图片URL（如有）',
  `jump_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转路径URL',
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order_id` ASC) USING BTREE,
  INDEX `idx_sender`(`sender_id` ASC) USING BTREE,
  INDEX `idx_receiver`(`receiver_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 298 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_order_status_logs
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_order_status_logs`;
CREATE TABLE `tb_jmz_order_status_logs`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `from_status` tinyint NULL DEFAULT NULL COMMENT '原状态',
  `to_status` tinyint NOT NULL COMMENT '新状态',
  `operator_id` bigint NOT NULL COMMENT '操作者用户ID',
  `operator_type` tinyint NOT NULL COMMENT '操作者类型：1-发布者，2-接手者，3-系统，4-客服',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注说明',
  `image_urls` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '凭证图片路径，多个用逗号分隔',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单需支付',
  `deposit` decimal(10, 2) NULL DEFAULT NULL COMMENT '保证金需支付',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order_id` ASC) USING BTREE,
  INDEX `idx_operator`(`operator_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_orders
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_orders`;
CREATE TABLE `tb_jmz_orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `publisher_id` bigint NOT NULL COMMENT '发布者用户ID',
  `taker_id` bigint NULL DEFAULT NULL COMMENT '接手者用户ID',
  `manager_id` bigint NULL DEFAULT NULL COMMENT '介入客服ID',
  `game_id` int NOT NULL COMMENT '游戏ID',
  `system_id` int NOT NULL COMMENT '系统ID',
  `server_id` int NULL DEFAULT NULL COMMENT '区服ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单标题',
  `boosting_type` tinyint NOT NULL DEFAULT 1 COMMENT '代练类型：1-代练，2-陪练',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '订单描述',
  `account_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '当前游戏账号信息',
  `price` decimal(10, 2) NOT NULL COMMENT '订单金额',
  `actual_amount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实际结算金额（包含平台服务费platform_fee）',
  `platform_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '平台收取金额',
  `security_deposit` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '安全保证金',
  `efficiency_deposit` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '效率保证金',
  `status` tinyint NULL DEFAULT 1 COMMENT '订单状态：1-未接手，2-代练中，3-待验收，5-已完成，6-已撤销，7-撤销中，8-待介入，9-介入中，10-已仲裁',
  `start_at` timestamp NULL DEFAULT NULL COMMENT '开始代练时间',
  `time_limit` int NULL DEFAULT NULL COMMENT '代练时限(小时)',
  `actual_at` timestamp NULL DEFAULT NULL COMMENT '实际完成时间(小时)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `paid` tinyint NOT NULL DEFAULT 0 COMMENT '0未付款，1已付款',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员内部备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_publisher`(`publisher_id` ASC) USING BTREE,
  INDEX `idx_taker`(`taker_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_game_name`(`game_id` ASC) USING BTREE,
  INDEX `idx_system_name`(`system_id` ASC) USING BTREE,
  INDEX `idx_server_name`(`server_id` ASC) USING BTREE,
  INDEX `idx_boosting_type`(`boosting_type` ASC) USING BTREE,
  INDEX `idx_game_id`(`game_id` ASC) USING BTREE,
  INDEX `idx_system_id`(`system_id` ASC) USING BTREE,
  INDEX `idx_server_id`(`server_id` ASC) USING BTREE,
  CONSTRAINT `fk_game_id` FOREIGN KEY (`game_id`) REFERENCES `tb_jmz_games` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_server_id` FOREIGN KEY (`server_id`) REFERENCES `tb_jmz_game_servers` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_system_id` FOREIGN KEY (`system_id`) REFERENCES `tb_jmz_systems` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_permission`;
CREATE TABLE `tb_jmz_permission`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `keyword` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `description` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_jmz_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_role`;
CREATE TABLE `tb_jmz_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `keyword` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `description` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_jmz_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_role_menu`;
CREATE TABLE `tb_jmz_role_menu`  (
  `role_id` int NOT NULL,
  `menu_id` int NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `FK_Reference_10`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_jmz_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_role_permission`;
CREATE TABLE `tb_jmz_role_permission`  (
  `role_id` int NOT NULL,
  `permission_id` int NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE,
  INDEX `FK_Reference_12`(`permission_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_jmz_systems
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_systems`;
CREATE TABLE `tb_jmz_systems`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统名称（如苹果端、安卓端）',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统图标',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_transactions
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_transactions`;
CREATE TABLE `tb_jmz_transactions`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作者用户ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联订单ID',
  `transaction_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易流水号',
  `type` tinyint NOT NULL COMMENT '交易类型：1-充值，2-订单收入，3-退款，10-提现，11-订单支出，12-罚款，20-冻结资金，30-解冻资金，31-解冻并扣除冻结金额',
  `amount` decimal(12, 2) NOT NULL COMMENT '交易金额',
  `balance_before` decimal(12, 2) NOT NULL COMMENT '交易前余额',
  `balance_after` decimal(12, 2) NOT NULL COMMENT '交易后余额',
  `status` tinyint NULL DEFAULT 1 COMMENT '交易状态：1-处理中，2-成功，3-失败',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '交易备注',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `frozen_before` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '冻结前金额',
  `frozen_after` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '冻结后金额',
  `idempotency_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '幂等键',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `transaction_no`(`transaction_no` ASC) USING BTREE,
  INDEX `idx_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_order`(`order_id` ASC) USING BTREE,
  INDEX `idx_transaction_no`(`transaction_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 216 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_user`;
CREATE TABLE `tb_jmz_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键，自增',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` tinyint NULL DEFAULT 1 COMMENT '性别（0-未知，1-男，2-女）',
  `status` tinyint NULL DEFAULT 1 COMMENT '用户状态（0-禁用，1-正常）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_boosting_enabled` tinyint NOT NULL DEFAULT 0 COMMENT '是否开启代练 0-否 1-是',
  `identity_verified` tinyint NOT NULL DEFAULT 0 COMMENT '实名认证状态：0-未认证，1-已认证',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1948281592799584257 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_jmz_user_accounts
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_user_accounts`;
CREATE TABLE `tb_jmz_user_accounts`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `balance` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `frozen_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '冻结金额',
  `total_income` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '总收入',
  `total_expense` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '总支出',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本号，用于乐观锁',
  `last_idempotency_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次操作幂等键',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_user`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_user_game_boosting
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_user_game_boosting`;
CREATE TABLE `tb_jmz_user_game_boosting`  (
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `game_id` int UNSIGNED NOT NULL COMMENT '游戏ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE INDEX `uniq_user_game`(`user_id` ASC, `game_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_game_id`(`game_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与游戏代打关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_jmz_user_identity_verification
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_user_identity_verification`;
CREATE TABLE `tb_jmz_user_identity_verification`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `id_card_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号码',
  `id_card_front_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证正面照片URL',
  `id_card_back_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证反面照片URL',
  `face_photo_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手持身份证照片URL',
  `verification_status` tinyint NOT NULL DEFAULT 0 COMMENT '认证状态：0-待审核，1-审核通过，2-审核拒绝',
  `reject_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `verifier_id` bigint NULL DEFAULT NULL COMMENT '审核员ID',
  `verified_at` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_id_card`(`id_card_number` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户实名认证表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_jmz_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_jmz_user_role`;
CREATE TABLE `tb_jmz_user_role`  (
  `user_id` bigint NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `fk_user_role_role_idx`(`role_id` ASC) USING BTREE,
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `tb_jmz_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `tb_jmz_user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
