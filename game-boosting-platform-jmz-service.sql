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

 Date: 24/07/2025 13:24:31
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
-- Records of tb_jmz_game_servers
-- ----------------------------
INSERT INTO `tb_jmz_game_servers` VALUES (1665, 1, 1, '1区王者独尊', 1, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1666, 1, 1, '2区绝代智谋', 2, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1667, 1, 1, '3区不羁之风', 3, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1668, 1, 1, '4区千金重弩', 4, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1669, 1, 1, '5区死亡绽放', 5, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1670, 1, 1, '6区最终兵器', 6, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1671, 1, 1, '7区叛逆吟游', 7, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1672, 1, 1, '8区暴走机关', 8, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1673, 1, 1, '9区欲望之月', 9, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1674, 1, 1, '10区风华绚烂', 10, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1675, 1, 1, '11区逍遥幻梦', 11, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1676, 1, 1, '12区恋之微风', 12, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1677, 1, 1, '13区和平守望', 13, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1678, 1, 1, '14区苍天龙鸣', 14, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1679, 1, 1, '15区天翔之龙', 15, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1680, 1, 1, '16区破云之龙', 16, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1681, 1, 1, '17区惊雷之龙', 17, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1682, 1, 1, '18区友情守护', 18, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1683, 1, 1, '19区豪情突进', 19, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1684, 1, 1, '20区激情迸发', 20, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1685, 1, 1, '21区正义豪腕', 21, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1686, 1, 1, '22区治愈微笑', 22, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1687, 1, 1, '23区绽放之舞', 23, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1688, 1, 1, '24区甜蜜恋风', 24, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1689, 1, 1, '25区星华缭乱', 25, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1690, 1, 1, '26区兼爱非攻', 26, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1691, 1, 1, '27区和平漫步', 27, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1692, 1, 1, '28区机关重炮', 28, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1693, 1, 1, '29区墨守成规', 29, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1694, 1, 1, '30区活力迸发', 30, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1695, 1, 1, '31区翻滚突袭', 31, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1696, 1, 1, '32区红莲爆弹', 32, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1697, 1, 1, '33区究极弩炮', 33, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1698, 1, 1, '34区诛心剑雨', 34, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1699, 1, 1, '35区灵魂冲击', 35, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1700, 1, 1, '36区偶像魅力', 36, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1701, 1, 1, '37区女王崇拜', 37, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1702, 1, 1, '38区王朝密令', 38, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1703, 1, 1, '39区圣光守护', 39, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1704, 1, 1, '40区誓约之盾', 40, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1705, 1, 1, '41区回旋打击', 41, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1706, 1, 1, '42区圣剑裁决', 42, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1707, 1, 1, '43区迟缓之箭', 43, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1708, 1, 1, '44区疾风之箭', 44, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1709, 1, 1, '45区寒冰箭雨', 45, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1710, 1, 1, '46区惩戒射击', 46, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1711, 1, 1, '47区勇气意志', 47, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1712, 1, 1, '48区苍破烈斩', 48, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1713, 1, 1, '49区突进之刃', 49, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1714, 1, 1, '50区绽放刀锋', 50, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1715, 1, 1, '51区残月利刃', 51, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1716, 1, 1, '52区月光剑舞', 52, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1717, 1, 1, '53区灼热之月', 53, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1718, 1, 1, '54区血月之弑', 54, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1719, 1, 1, '55区疯狂弹幕', 55, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1720, 1, 1, '56区加农派对', 56, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1721, 1, 1, '57区机关迷城', 57, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1722, 1, 1, '58区青莲剑歌', 58, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1723, 1, 1, '59区神来之笔', 59, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1724, 1, 1, '60区逆袭之刃', 60, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1725, 1, 1, '61区不羁豪气', 61, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1726, 1, 1, '62区搏击拳套', 62, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1727, 1, 1, '63区吸血之镰', 63, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1728, 1, 1, '64区秘义之剑', 64, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1729, 1, 1, '65区风暴巨剑', 65, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1730, 1, 1, '66区日蚀之锤', 66, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1731, 1, 1, '67区狂暴双刃', 67, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1732, 1, 1, '68区凶残之力', 68, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1733, 1, 1, '69区破魔之刀', 69, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1734, 1, 1, '70区破灭君主', 70, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1735, 1, 1, '71区名刀正宗', 71, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1736, 1, 1, '72区泣血之刃', 72, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1737, 1, 1, '73区无尽刀锋', 73, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1738, 1, 1, '74区三圣之力', 74, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1739, 1, 1, '75区黑色战斧', 75, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1740, 1, 1, '76区影刃破军', 76, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1741, 1, 1, '77区咒术典籍', 77, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1742, 1, 1, '78区炼金护符', 78, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1743, 1, 1, '79区圣者法典', 79, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1744, 1, 1, '80区灵魂长卷', 80, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1745, 1, 1, '81区血族之书', 81, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1746, 1, 1, '82区光辉之剑', 82, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1747, 1, 1, '83区小丑面罩', 83, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1748, 1, 1, '84区进化水晶', 84, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1749, 1, 1, '85区炽热之拥', 85, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1750, 1, 1, '86区虚无法杖', 86, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1751, 1, 1, '87区回响之杖', 87, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1752, 1, 1, '88区冰霜法杖', 88, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1753, 1, 1, '89区奇迹幻视', 89, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1754, 1, 1, '90区巫术法杖', 90, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1755, 1, 1, '91区时之预言', 91, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1756, 1, 1, '92区贤者之书', 92, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1757, 1, 1, '93区海蓝宝石', 93, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1758, 1, 1, '94区绯红玛瑙', 94, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1759, 1, 1, '95区抗魔披风', 95, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1760, 1, 1, '96区生命宝珠', 96, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1761, 1, 1, '97区杀戮之甲', 97, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1762, 1, 1, '98区力量腰带', 98, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1763, 1, 1, '99区熔炼之心', 99, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1764, 1, 1, '100区神隐斗篷', 100, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1765, 1, 1, '101区雪山圆盾', 101, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1766, 1, 1, '102区军团荣耀', 102, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1767, 1, 1, '103区反伤刺甲', 103, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1768, 1, 1, '104区红莲斗篷', 104, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1769, 1, 1, '105区霸者重装', 105, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1770, 1, 1, '106区敬畏虚空', 106, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1771, 1, 1, '107区振兴之铠', 107, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1772, 1, 1, '108区魔女面纱', 108, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1773, 1, 1, '109区寒冰之心', 109, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1774, 1, 1, '110区贤者庇护', 110, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1775, 1, 1, '111区暴烈之甲', 111, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1776, 1, 1, '112区神速之靴', 112, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1777, 1, 1, '113区影忍之足', 113, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1778, 1, 1, '114区冷静抵抗', 114, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1779, 1, 1, '115区急速秘法', 115, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1780, 1, 1, '116区召唤战场', 116, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1781, 1, 1, '117区通天之塔', 117, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1782, 1, 1, '118区疾步之靴', 118, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1783, 1, 1, '119区狩猎宽刃', 119, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1784, 1, 1, '120区符文大剑', 120, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1785, 1, 1, '121区巨人之握', 121, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1786, 1, 1, '122区贪婪之噬', 122, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1787, 1, 1, '123区追击刀锋', 123, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1788, 1, 1, '124区巡守利斧', 124, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1789, 1, 1, '125区游击弯刀', 125, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1790, 1, 1, '126区幽灵疾步', 126, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1791, 1, 1, '127区心灵治疗', 127, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1792, 1, 1, '128区重力眩晕', 128, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1793, 1, 1, '129区自然净化', 129, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1794, 1, 1, '130区振奋冲击', 130, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1795, 1, 1, '131区圣骑王者', 131, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1796, 1, 1, '132区精灵冰弓', 132, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1797, 1, 1, '133区魔法大师', 133, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1798, 1, 1, '134区地狱岩魂', 134, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1799, 1, 1, '135区万圣前夜', 135, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1800, 1, 1, '136区忍者炎影', 136, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1801, 1, 1, '137区未来纪元', 137, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1802, 1, 1, '138区皇家上将', 138, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1803, 1, 1, '139区仁德义枪', 139, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1804, 1, 1, '140区禁血狂兽', 140, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1805, 1, 1, '141区一骑当千', 141, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1806, 1, 1, '142区单刀赴会', 142, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1807, 1, 1, '143区青龙偃月', 143, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1808, 1, 1, '144区侠客行', 144, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1809, 1, 1, '145区将进酒', 145, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1810, 1, 1, '146区强化散弹', 146, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1811, 1, 1, '147区双重射击', 147, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1812, 1, 1, '148区震荡射击', 148, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1813, 1, 1, '149区万象初新', 149, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1814, 1, 1, '150区黑暗潜能', 150, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1815, 1, 1, '151区画地为牢', 151, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1816, 1, 1, '152区狂意杀戮', 152, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1817, 1, 1, '153区守护机关', 153, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1818, 1, 1, '154区崩裂践踏', 154, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1819, 1, 1, '155区狂兽血性', 155, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1820, 1, 1, '156区饕餮血统', 156, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1821, 1, 1, '157区方天画斩', 157, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1822, 1, 1, '158区贪狼之握', 158, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1823, 1, 1, '159区魔神降世', 159, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1824, 1, 1, '160区大圣神威', 160, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1825, 1, 1, '161区七十二变', 161, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1826, 1, 1, '162区斗战冲锋', 162, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1827, 1, 1, '163区如意金箍', 163, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1828, 1, 1, '164区战争践踏', 164, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1829, 1, 1, '165区大地震撼', 165, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1830, 1, 1, '166区横行霸道', 166, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1831, 1, 1, '167区野性意志', 167, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1832, 1, 1, '168区语花印', 168, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1833, 1, 1, '169区落红雨', 169, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1834, 1, 1, '170区缘心结', 170, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1835, 1, 1, '171区绽风华', 171, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1836, 1, 1, '172区齐天大圣', 172, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1837, 1, 1, '173区精英酋长', 173, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1838, 1, 1, '174区半神之弓', 174, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1839, 1, 1, '175区摘星楼', 175, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1840, 1, 1, '176区王者峡谷', 176, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1841, 1, 1, '177区长平战场', 177, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1842, 1, 1, '178区海洋之心', 178, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1843, 1, 1, '179区白衣执事', 179, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1844, 1, 1, '180区天鹅之梦', 180, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1845, 1, 1, '181区大魔术师', 181, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1846, 1, 1, '182区职棒王牌', 182, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1847, 1, 1, '183区天魔缭乱', 183, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1848, 1, 1, '184区紫霞仙子', 184, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1849, 1, 1, '185区教廷特使', 185, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1850, 1, 1, '186区我是歌手', 186, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1851, 1, 1, '187区天堂福音', 187, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1852, 1, 1, '188区波斯王子', 188, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1853, 1, 1, '189区荒野镖客', 189, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1854, 1, 1, '190区西部镖客', 190, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1855, 1, 1, '191区荣耀王者', 191, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1856, 1, 1, '192区哥特玫瑰', 192, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1857, 1, 1, '193区绯红之刃', 193, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1858, 1, 1, '194区鬼之武者', 194, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1859, 1, 1, '195区地狱之火', 195, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1860, 1, 1, '196区天启骑士', 196, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1861, 1, 1, '197区花好人间', 197, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1862, 1, 1, '198区大秦宣后', 198, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1863, 1, 1, '199区万事如意', 199, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1864, 1, 1, '200区龙腾万里', 200, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1865, 1, 1, '201区密探之力', 201, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1866, 1, 1, '202区谍影重重', 202, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1867, 1, 1, '203区无间刃锋', 203, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1868, 1, 1, '204区制裁仪式', 204, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1869, 1, 1, '205区虚空吞没', 205, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1870, 1, 1, '206区湮灭之锁', 206, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1871, 1, 1, '207区轮回吞噬', 207, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1872, 1, 1, '208区飞翔龙炎', 208, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1873, 1, 1, '209区花蝶之扇', 209, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1874, 1, 1, '210区必杀忍蜂', 210, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1875, 1, 1, '211区流刀舞术', 211, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1876, 1, 1, '212区飞鹰攻击', 212, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1877, 1, 1, '213区清风之刃', 213, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1878, 1, 1, '214区飞鹰急袭', 214, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1879, 1, 1, '215区大唐传奇', 215, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1880, 1, 1, '216区桃圆结义', 216, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1881, 1, 1, '217区燎原火', 217, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1882, 1, 1, '218区血之咆哮', 218, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1883, 1, 1, '219区天之劫', 219, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1884, 1, 1, '220区不死之王', 220, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1885, 1, 1, '221区荆棘之心', 221, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1886, 1, 1, '222区魔女斗篷', 222, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1887, 1, 1, '223区觉醒水晶', 223, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1888, 1, 1, '224区新月残像', 224, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1889, 1, 1, '225区神圣心石', 225, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1890, 1, 1, '226区死海文书', 226, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1891, 1, 1, '227区王的铁面', 227, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1892, 1, 1, '228区王城密探', 228, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1893, 1, 1, '229区虚灵城判', 229, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1894, 1, 1, '230区鹰之守护', 230, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1895, 1, 1, '231区明媚烈焰', 231, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1896, 1, 1, '232区地府判官', 232, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1897, 1, 1, '233区特种小队', 233, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1898, 1, 1, '234区狮心王', 234, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1899, 1, 1, '235区千年之狐', 235, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1900, 1, 1, '236区灾厄尽头', 236, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1901, 1, 1, '237区稷下战场', 237, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1902, 1, 1, '238区传送之扉', 238, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1903, 1, 1, '239区杀戮回忆', 239, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1904, 1, 1, '240区机关迷城', 240, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1905, 1, 1, '241区隐秘牢笼', 241, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1906, 1, 1, '242区意志传承', 242, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1907, 1, 1, '243区时光倒流', 243, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1908, 1, 1, '244区长平攻防', 244, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1909, 1, 1, '245区血浓于血', 245, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1910, 1, 1, '246区魔道探索', 246, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1911, 1, 1, '247区魔道信仰', 247, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1912, 1, 1, '248区博学魔道', 248, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1913, 1, 1, '249区王族血统', 249, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1914, 1, 1, '250区天潢贵胄', 250, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1915, 1, 1, '251区苍狼末裔', 251, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1916, 1, 1, '252区根源之目', 252, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1917, 1, 1, '253区至高创世', 253, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1918, 1, 1, '254区桀骜炎枪', 254, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1919, 1, 1, '255区淬命双剑', 255, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1920, 1, 1, '256区圣域余晖', 256, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1921, 1, 1, '257区天籁弦音', 257, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1922, 1, 1, '258区炼金大师', 258, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1923, 1, 1, '259区噬灭日蚀', 259, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1924, 1, 1, '260区万物有灵', 260, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1925, 1, 1, '261区绝代智谋', 261, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1926, 1, 1, '262区燃魂重炮', 262, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1927, 1, 1, '263区追击潜能', 263, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1928, 1, 1, '264区警戒地雷', 264, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1929, 1, 1, '265区重装炮台', 265, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1930, 1, 1, '266区玄微之灵', 266, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1931, 1, 1, '267区纵横兵法', 267, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1932, 1, 1, '268区先知奇谋', 268, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1933, 1, 1, '269区气数终焉', 269, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1934, 1, 1, '270区太古雷霆', 270, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1935, 1, 1, '271区全能宗师', 271, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1936, 1, 1, '272区造化敕令', 272, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1937, 1, 1, '273区制裁神谕', 273, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1938, 1, 1, '274区炙炼火种', 274, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1939, 1, 1, '275区火焰尖枪', 275, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1940, 1, 1, '276区混天绫束', 276, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1941, 1, 1, '277区乾坤天降', 277, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1942, 1, 1, '278区提线傀儡', 278, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1943, 1, 1, '279区虚妄破灭', 279, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1944, 1, 1, '280区根源之目', 280, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1945, 1, 1, '281区百兽陷阱', 281, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1946, 1, 1, '282区可汗狂猎', 282, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1947, 1, 1, '283区树神护佑', 283, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1948, 1, 1, '284区楚歌起', 284, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1949, 1, 1, '285区大风来', 285, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1950, 1, 1, '286区阵前舞', 286, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1951, 1, 1, '287区密探谛听', 287, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1952, 1, 1, '288区秘剑胧刀', 288, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1953, 1, 1, '289区神梦一刀', 289, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1954, 1, 1, '290区燕返居合', 290, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1955, 1, 1, '291区华丽左轮', 291, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1956, 1, 1, '292区漫游之枪', 292, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1957, 1, 1, '293区狂热弹幕', 293, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1958, 1, 1, '294区真神觉醒', 294, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1959, 1, 1, '295区神圣进军', 295, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1960, 1, 1, '296区贯穿之枪', 296, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1961, 1, 1, '297区敬畏圣盾', 297, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1962, 1, 1, '298区奔狼纹章', 298, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1963, 1, 1, '299区不样征兆', 299, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1964, 1, 1, '300区梦魇之牙', 300, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1965, 1, 1, '301区惊鸿之笔', 301, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1966, 1, 1, '302区隐龙之影', 302, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1967, 1, 1, '303区爆炸心跳', 303, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1968, 1, 1, '304区节奏热浪', 304, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1969, 1, 1, '305区踏雪寻梅', 305, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1970, 1, 1, '306区虎啸风生', 306, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1971, 1, 1, '307区天元之弈', 307, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1972, 1, 1, '308区霓裳风华', 308, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1973, 1, 1, '309区孤鹜断霞', 309, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1974, 1, 1, '310区寂灭之心', 310, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1975, 1, 1, '311区无尽之盾', 311, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1976, 1, 1, '312区灵魂卜劫', 312, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1977, 1, 1, '313区海之征途', 313, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1978, 1, 1, '314区曙光守护', 314, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1979, 1, 1, '315区云端筑梦', 315, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1980, 1, 1, '316区千机万变', 316, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1981, 1, 1, '317区特工魅影', 317, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1982, 1, 1, '318区心灵骇客', 318, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1983, 1, 1, '319区无乡幽灵', 319, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1984, 1, 1, '320区幻乐之宴', 320, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1985, 1, 1, '321区一夫当关', 321, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1986, 1, 1, '322区静默之语', 322, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1987, 1, 1, '323区利刃藏锋', 323, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1988, 1, 1, '324区天地化盾', 324, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1989, 1, 1, '325区幽影之咬', 325, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1990, 1, 1, '326区笔走龙蛇', 326, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1991, 1, 1, '327区劈风斩浪', 327, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1992, 1, 1, '328区善恶诊断', 328, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1993, 1, 1, '329区无畏战车', 329, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1994, 1, 1, '330区灼日之矢', 330, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1995, 1, 1, '331区碎星之锤', 331, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1996, 1, 1, '332区末世赤瞳', 332, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1997, 1, 1, '333区凤鸣指环', 333, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1998, 1, 1, '334区冰痕之握', 334, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (1999, 1, 1, '335区日冕时刻', 335, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2000, 1, 1, '336区宗师之力', 336, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2001, 1, 1, '337区魅影面罩', 337, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2002, 1, 1, '338区风灵纹章', 338, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2003, 1, 1, '339区逐日之弓', 339, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2004, 1, 1, '340区破碎圣杯', 340, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2005, 1, 1, '341区狂热序章', 341, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2006, 1, 1, '342区辉煌指引', 342, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2007, 1, 1, '343区东风破袭', 343, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2008, 1, 1, '344区比翼同心', 344, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2009, 1, 1, '345区决断之桥', 345, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2010, 1, 1, '346区落日余晖', 346, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2011, 1, 1, '347区荒芜之域', 347, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2012, 1, 1, '348区海潮怒吼', 348, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2013, 1, 1, '349区浩劫磁场', 349, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2014, 1, 1, '350区强袭风暴', 350, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2015, 1, 1, '351区东风祭坛', 351, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2016, 1, 1, '352区勇士之地', 352, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2017, 1, 1, '353区金庭遗梦', 353, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2018, 1, 1, '354区雁帐美酒', 354, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2019, 1, 1, '355区冰原苍狼', 355, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2020, 1, 1, '356区大明宫词', 356, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2021, 1, 1, '357区河洛一方', 357, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2022, 1, 1, '358区干窟之城', 358, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2023, 1, 1, '359区京都来客', 359, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2024, 1, 1, '360区血族巢穴', 360, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2025, 1, 1, '361区鹰眼追击', 361, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2026, 1, 1, '362区草丛隐匿', 362, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2027, 1, 1, '363区宿命传承', 363, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2028, 1, 1, '364区野兽之痕', 364, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2029, 1, 1, '365区自然调和', 365, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2030, 1, 1, '366区浮生虚空', 366, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2031, 1, 1, '367区玲珑心眼', 367, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2032, 1, 1, '368区贪婪罪名', 368, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2033, 1, 1, '369区异变领土', 369, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2034, 1, 1, '370区心为祸源', 370, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2035, 1, 1, '371区灵魂乐师', 371, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2036, 1, 1, '372区月光女神', 372, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2037, 1, 1, '373区玩偶法师', 373, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2038, 1, 1, '374区社丹方士', 374, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2039, 1, 1, '375区爆弹怪猫', 375, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2040, 1, 1, '376区长安秘使', 376, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2041, 1, 1, '377区白发名将', 377, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2042, 1, 1, '378区逍遥书生', 378, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2043, 1, 1, '379区寻宝奇侠', 379, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2044, 1, 1, '380区王者出征', 380, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2045, 1, 1, '381区献祭之名', 381, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2046, 1, 1, '382区枭雄纷争', 382, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2047, 1, 1, '383区长生秘药', 383, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2048, 1, 1, '384区彼岸冥想', 384, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2049, 1, 1, '385区灵山之巅', 385, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2050, 1, 1, '386区群英荟萃', 386, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2051, 1, 1, '387区芳心狩猎', 387, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2052, 1, 1, '388区霸者无疆', 388, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2053, 1, 1, '389区梦境回声', 389, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2054, 1, 1, '390区敬畏存心', 390, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2055, 1, 1, '391区圣殿骑士', 391, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2056, 1, 1, '392区忍之武者', 392, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2057, 1, 1, '393区暗影伯爵', 393, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2058, 1, 1, '394区奇迹天使', 394, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2059, 1, 1, '395区虚妄神明', 395, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2060, 1, 1, '396区海的新娘', 396, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2061, 1, 1, '397区守灯旅客', 397, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2062, 1, 1, '398区金刚丽人', 398, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2063, 1, 1, '399区恶德怪医', 399, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2064, 1, 1, '400区时间之子', 400, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2065, 1, 1, '401区长虹贯日', 401, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2066, 1, 1, '402区三足鼎立', 402, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2067, 1, 1, '403区三顾茅庐', 403, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2068, 1, 1, '404区铁索连环', 404, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2069, 1, 1, '405区魏武挥鞭', 405, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2070, 1, 1, '406区华灯初上', 406, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2071, 1, 1, '407区顾曲周郎', 407, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2072, 1, 1, '408区星罗棋布', 408, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2073, 1, 1, '409区绮年玉貌', 409, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2074, 1, 1, '410区飞阁流丹', 410, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2075, 1, 1, '411区弯弓落月', 411, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2076, 1, 1, '412区巧夺天工', 412, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2077, 1, 1, '413区锦衣夜行', 413, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2078, 1, 1, '414区烟波浩渺', 414, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2079, 1, 1, '415区雷霆万钧', 415, '2025-07-23 15:32:54', '2025-07-23 15:32:54');
INSERT INTO `tb_jmz_game_servers` VALUES (2080, 1, 1, '416区风云乍起', 416, '2025-07-23 15:32:54', '2025-07-23 15:32:54');

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
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_jmz_game_systems
-- ----------------------------
INSERT INTO `tb_jmz_game_systems` VALUES (24, 1, 1);
INSERT INTO `tb_jmz_game_systems` VALUES (25, 1, 2);
INSERT INTO `tb_jmz_game_systems` VALUES (26, 1, 3);
INSERT INTO `tb_jmz_game_systems` VALUES (27, 1, 4);
INSERT INTO `tb_jmz_game_systems` VALUES (28, 3, 5);
INSERT INTO `tb_jmz_game_systems` VALUES (29, 4, 1);
INSERT INTO `tb_jmz_game_systems` VALUES (30, 4, 2);
INSERT INTO `tb_jmz_game_systems` VALUES (31, 4, 3);
INSERT INTO `tb_jmz_game_systems` VALUES (32, 4, 4);
INSERT INTO `tb_jmz_game_systems` VALUES (34, 5, 1);
INSERT INTO `tb_jmz_game_systems` VALUES (35, 5, 2);
INSERT INTO `tb_jmz_game_systems` VALUES (36, 5, 3);
INSERT INTO `tb_jmz_game_systems` VALUES (37, 5, 4);
INSERT INTO `tb_jmz_game_systems` VALUES (33, 11, 5);

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
-- Records of tb_jmz_games
-- ----------------------------
INSERT INTO `tb_jmz_games` VALUES (1, '王者荣耀', '2025-07-23/d9df3025ea8c4c3fbc8806edff457601.png', 1, 0, '2025-07-16 13:28:26', '2025-07-23 14:43:33');
INSERT INTO `tb_jmz_games` VALUES (3, '原神', '2025-07-23/fc3bbfb6b95545e5b772f2eee1ac7eb5.png', 1, 1, '2025-07-16 16:01:29', '2025-07-23 14:43:40');
INSERT INTO `tb_jmz_games` VALUES (4, '火影忍者', '2025-07-23/84c48066286a4df0a00f762d1265646b.png', 1, 2, '2025-07-16 10:49:34', '2025-07-23 14:43:52');
INSERT INTO `tb_jmz_games` VALUES (5, '穿越火线', '2025-07-23/192e65affb1041e4855cabeb5143a3a9.png', 1, 4, '2025-07-16 10:55:03', '2025-07-23 14:44:05');
INSERT INTO `tb_jmz_games` VALUES (8, '永劫无间', '2025-07-23/87e2f62922f24a118fca19fc7e30d77a.png', 1, 5, '2025-07-16 14:14:41', '2025-07-23 14:22:04');
INSERT INTO `tb_jmz_games` VALUES (9, 'LOL手游', '2025-07-23/2fb7443cc9ff4a179b8fb4247c2534ad.png', 1, 6, '2025-07-23 14:22:58', '2025-07-23 14:22:58');
INSERT INTO `tb_jmz_games` VALUES (10, '无畏契约', '2025-07-23/6a661f601f9f487bbe1426f6a3308f84.png', 1, 7, '2025-07-23 14:23:34', '2025-07-23 14:23:34');
INSERT INTO `tb_jmz_games` VALUES (11, '三角洲行动', '2025-07-23/6bf82a52880e44a8a15f99e70f7adf2a.png', 1, 3, '2025-07-23 14:24:28', '2025-07-23 14:43:59');
INSERT INTO `tb_jmz_games` VALUES (12, '英雄联盟', '2025-07-23/63e3c3a45b1346beb3291e9b2e78474c.png', 1, 8, '2025-07-23 14:24:54', '2025-07-23 14:25:00');
INSERT INTO `tb_jmz_games` VALUES (13, '第五人格', '2025-07-23/7862c702ae664d9198eb3dff8bfb12c2.png', 1, 11, '2025-07-23 14:25:31', '2025-07-23 14:40:53');
INSERT INTO `tb_jmz_games` VALUES (14, '暗区突围', '2025-07-23/61fb6a5109564e7aa6d5ac689f747017.png', 1, 9, '2025-07-23 14:26:01', '2025-07-23 14:26:07');
INSERT INTO `tb_jmz_games` VALUES (15, '和平精英', '2025-07-23/3246e51be225433fa2e7291c0f52b8b0.png', 1, 10, '2025-07-23 14:26:34', '2025-07-23 14:26:39');
INSERT INTO `tb_jmz_games` VALUES (16, '金铲铲之战', '2025-07-23/125db301fdcc4ffc85ade26a444b5e59.png', 1, 15, '2025-07-23 14:27:08', '2025-07-23 14:42:09');
INSERT INTO `tb_jmz_games` VALUES (17, '蛋仔派对', '2025-07-23/3bdd7730d1ba47e59bae3e06ffdff432.png', 1, 12, '2025-07-23 14:27:38', '2025-07-23 14:27:44');
INSERT INTO `tb_jmz_games` VALUES (18, '易水寒', '2025-07-23/4488e7ddcb234ee2b6ff108112a7a55c.png', 1, 13, '2025-07-23 14:41:24', '2025-07-23 14:41:24');
INSERT INTO `tb_jmz_games` VALUES (19, '潮鸣', '2025-07-23/0d971920543c43f999fe0a302db786b7.png', 1, 14, '2025-07-23 14:41:40', '2025-07-23 14:41:48');

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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tb_jmz_menu_ibfk_1`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `tb_jmz_menu_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `tb_jmz_menu` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_jmz_menu
-- ----------------------------
INSERT INTO `tb_jmz_menu` VALUES (1, '系统管理', 'system', NULL, NULL, 0, 'List', 'users', 1, NULL, '2025-07-08 18:34:45', '2025-07-11 17:35:22');
INSERT INTO `tb_jmz_menu` VALUES (2, '用户管理', 'users', '/src/views/system/users/Index.vue', 1, 1, 'User', NULL, 1, 'system:user:list', '2025-07-08 18:34:45', '2025-07-11 23:49:12');
INSERT INTO `tb_jmz_menu` VALUES (3, '角色管理', 'roles', '/src/views/system/roles/Index.vue', 1, 1, 'Avatar', NULL, 2, 'system:role:list', '2025-07-08 18:34:45', '2025-07-11 23:48:29');
INSERT INTO `tb_jmz_menu` VALUES (4, '权限管理', 'permissions', '/src/views/system/permissions/Index.vue', 1, 1, 'Unlock', NULL, 3, 'system:permission:list', '2025-07-08 18:34:45', '2025-07-11 23:49:25');
INSERT INTO `tb_jmz_menu` VALUES (5, '菜单管理', 'menus', '/src/views/system/menus/Index.vue', 1, 1, 'Grid', NULL, 4, 'system:menu:list', '2025-07-08 18:34:45', '2025-07-11 23:50:03');
INSERT INTO `tb_jmz_menu` VALUES (6, '仪表盘', 'dashboard', '/src/views/dashboard/Index.vue', NULL, 1, 'dashboard', NULL, 5, 'system:dashboard:view', '2025-07-08 18:34:45', '2025-07-17 09:45:50');
INSERT INTO `tb_jmz_menu` VALUES (7, '设置', 'settings', NULL, NULL, 0, 'Setting', 'settings/system', 5, NULL, '2025-07-08 18:34:45', '2025-07-17 09:45:45');
INSERT INTO `tb_jmz_menu` VALUES (8, '个人信息设置', 'prefile', '/src/views/settings/prefile/Index.vue', 7, 1, NULL, NULL, 1, '', '2025-07-08 18:34:45', '2025-07-24 01:21:51');
INSERT INTO `tb_jmz_menu` VALUES (11, '代练订单管理', 'order-management', '', NULL, 0, 'CreditCard', NULL, 4, '', '2025-07-11 22:13:26', '2025-07-17 09:45:20');
INSERT INTO `tb_jmz_menu` VALUES (12, '异常订单', 'exception-order', '/src/views/platform/order-management/exception-order/Index.vue', 11, 1, 'Tickets', NULL, 1, 'order-management:order', '2025-07-11 23:46:12', '2025-07-22 19:10:05');
INSERT INTO `tb_jmz_menu` VALUES (13, '代练游戏管理', 'game-mangement', '', NULL, 0, 'Notebook', NULL, 2, '', '2025-07-14 17:15:22', '2025-07-14 17:23:13');
INSERT INTO `tb_jmz_menu` VALUES (14, '游戏管理', 'game', '/src/views/platform/game-management/games/Index.vue', 13, 1, 'Service', NULL, 1, 'game-management:games', '2025-07-14 17:22:24', '2025-07-14 18:36:57');
INSERT INTO `tb_jmz_menu` VALUES (15, '游戏系统管理', 'systems', '/src/views/platform/game-management/systems/Index.vue', 13, 1, 'Cellphone', NULL, 2, 'game-management:systems', '2025-07-14 18:25:06', '2025-07-14 18:25:06');
INSERT INTO `tb_jmz_menu` VALUES (16, '服务区管理', 'servers', '/src/views/platform/game-management/servers/Index.vue', 13, 1, 'Select', NULL, 3, 'game-management:servers', '2025-07-14 18:28:22', '2025-07-16 16:05:17');
INSERT INTO `tb_jmz_menu` VALUES (17, '用户账户管理', 'account', '', NULL, 0, 'Wallet', NULL, 3, 'user-account', '2025-07-17 09:33:35', '2025-07-17 09:46:50');
INSERT INTO `tb_jmz_menu` VALUES (18, '账户管理', 'account', '/src/views/user-account/account/Index.vue', 17, 1, 'WalletFilled', NULL, 0, 'user-account:account', '2025-07-17 09:40:29', '2025-07-17 09:49:57');
INSERT INTO `tb_jmz_menu` VALUES (19, '交易记录', 'transaction', '/src/views/user-account/transaction/Index.vue', 17, 1, 'DocumentCopy', NULL, 1, 'user-account:transaction', '2025-07-17 09:43:26', '2025-07-17 09:50:00');
INSERT INTO `tb_jmz_menu` VALUES (21, '介入订单管理', 'exception-order-management', '/src/views/platform/order-management/exception-order-management/Index.vue', 11, 1, 'Platform', NULL, 2, '', '2025-07-21 18:26:09', '2025-07-22 19:07:16');
INSERT INTO `tb_jmz_menu` VALUES (22, '订单管理', 'order', '/src/views/platform/order-management/Index.vue', 11, 1, 'DocumentCopy', NULL, 0, '', '2025-07-22 19:05:26', '2025-07-22 19:05:26');

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
) ENGINE = InnoDB AUTO_INCREMENT = 208 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_jmz_messages
-- ----------------------------
INSERT INTO `tb_jmz_messages` VALUES (200, 14, 0, 1948235993966456832, 2, 1, '您的订单已创建成功！', NULL, NULL, 0, '2025-07-24 05:03:35');
INSERT INTO `tb_jmz_messages` VALUES (201, 15, 0, 1948235993966456832, 2, 1, '您的订单已创建成功！', NULL, NULL, 0, '2025-07-24 05:04:57');
INSERT INTO `tb_jmz_messages` VALUES (202, 16, 0, 1948235993966456832, 2, 1, '您的订单已创建成功！', NULL, NULL, 0, '2025-07-24 05:05:57');
INSERT INTO `tb_jmz_messages` VALUES (203, 17, 0, 1948235993966456832, 2, 1, '您的订单已创建成功！', NULL, NULL, 0, '2025-07-24 05:07:13');
INSERT INTO `tb_jmz_messages` VALUES (204, 18, 0, 1948235993966456832, 2, 1, '您的订单已创建成功！', NULL, NULL, 0, '2025-07-24 05:08:12');
INSERT INTO `tb_jmz_messages` VALUES (205, 19, 0, 1948235993966456832, 2, 1, '您的订单已创建成功！', NULL, NULL, 0, '2025-07-24 05:09:08');
INSERT INTO `tb_jmz_messages` VALUES (206, 20, 0, 1948235993966456832, 2, 1, '您的订单已创建成功！', NULL, NULL, 0, '2025-07-24 05:10:13');
INSERT INTO `tb_jmz_messages` VALUES (207, 21, 0, 1948235993966456832, 2, 1, '您的订单已创建成功！', NULL, NULL, 0, '2025-07-24 05:11:42');

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
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_jmz_order_status_logs
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_jmz_orders
-- ----------------------------
INSERT INTO `tb_jmz_orders` VALUES (14, 'ORD1948247665947045888', 1948235993966456832, NULL, NULL, 1, 1, 1667, '5034分 - 7000分 铭文150', 2, '1:密码写的是号主的联系途径，账号写的是联系方式。\n2:根据标题上传对应首图、进度图、尾图。战力单完单图需额外上传去掉巅峰挑战赛的分数截图\n3:走私单,一经发现,扣除双金,冻结账号,全网拉黑\n密码是号主的联系渠道！！账号是联系方式！！\n\n【 禁止泄露接单价格或接单方式 , 否则扣除全部保证金0代练费】\n1、接单后30分钟内必须上号开打传首图,否则无首图打完后结算50%代练费\n2、荣耀战力订单禁止打巅峰挑战赛,不可以掉原段位以及巅峰赛分数,否则按市场价扣除\n3、指定英雄使用率不低于70% 并上传指定英雄比赛记录图和被Ban被抢截图,不能掉战力\n4、接单后传首图，防止号主恶意顶号不提供截图,代练期间一定要上传进度图，完成上传带好友榜完单截图，注意订单完成时间。\n5、请勿动用号内物品，禁止拉私单。', '账号：\n密码：\n区服：\n角色名：\n联系方式：', 100.00, NULL, 5.00, 10.00, 10.00, 1, NULL, 100, NULL, '2025-07-24 05:03:35', '2025-07-24 05:03:35', 0);
INSERT INTO `tb_jmz_orders` VALUES (15, 'ORD1948248011444449280', 1948235993966456832, NULL, NULL, 1, 2, NULL, '伽罗◆▍战力4750分-7000分 段位:星耀2 巅峰赛: 铭文150 超时不扣钱打完就验收', 1, '1:完成图没问题联系我们客服会秒结算（好友天梯榜首尾图）\n2:接单第一时间联系号主，只要号主不找我们反馈问题，这边不会平白无故扣一分钱\n3:如果接单不打并怂恿号主走私单,一经发现,扣除双金,冻结账号,永久拉黑\n4:禁止开挂、盗号，发现后立马冻结平台,并调取你个人信息和提现账户信息,举报到网监局和当地派出所,你会有案底,同时还要追究你的责任,并赔偿账号全部损失.', '接单后请30分钟内上号开打,完成后进裙找管理员秒验收 \n\n1、未经允许禁止使用号内物品,禁止私下交易 \n\n2、接单和完成订单务必上传好友排行榜首图和完成图\n\n3、禁止将接单价格和平台透露给号主,违反扣全部保证金\n\n4、微区需联系号主,电话在角色名后面,选扫码登,态度好些 \n\n5、Q区先进游戏再从后台登账号或卸载Q登,不可登玩家Q \n\n6、若是指定英雄订单,上传指定英雄比赛记录图和被Ban被抢截图,不能掉战力\n\n7、订单出现问题可以加群【亿岭尔留思久巴尔留久】', 80.00, NULL, 4.00, 10.00, 10.00, 1, NULL, 20, NULL, '2025-07-24 05:04:57', '2025-07-24 05:04:57', 0);
INSERT INTO `tb_jmz_orders` VALUES (16, 'ORD1948248262016364544', 1948235993966456832, NULL, NULL, 1, 1, 1689, '上官婉儿小国标现在8339上榜9388 铭文150巅峰1688分王者58星包本周小国标拿不到标打不完中途撤单00结算扣效率', 1, 'QQ区登录:*,进入游戏,选择“切换账号\"“添加账号”后联系玩家进行辅助验证\nWX区登录:*联系玩家进行辅助验证:*登录WX号,再进入游戏点击与WX好友玩即可\n①接单后有疑问可查看锦囊内常见问题,或者咨询在线客服②排位订单上号记得上传好友排行榜的首图,打完上传好友排行榜的完单图。\n③代练期间请勿私自使用号内任何物品,包括钻石、点卷金币、体验卡等等。\n④切勿使用外挂,不允许打广告、挂机、恶意骂人等恶意行为。\n⑤私自联系或者回复号主以及号主好友(禁止私下交易),违反扣安全保证金。', '账号：\n密码：\n区服：\n角色名：\n联系方式：', 200.00, NULL, 10.00, 30.00, 30.00, 1, NULL, 24, NULL, '2025-07-24 05:05:57', '2025-07-24 05:05:57', 0);
INSERT INTO `tb_jmz_orders` VALUES (17, 'ORD1948248578736648192', 1948235993966456832, NULL, NULL, 4, 1, NULL, '通天代低于[70%]胜率 结算一半 中途撤单0结算 花木兰◆▍星耀4 4星-王者1星 铭文150 打完就验收', 1, '1:完成图没问题联系我们客服会秒结算（好友天梯榜首尾图）\n2:接单第一时间联系号主，只要号主不找我们反馈问题，这边不会平白无故扣一分钱\n3:如果接单不打并怂恿号主走私单,一经发现,扣除双金,冻结账号,永久拉黑\n4:禁止开挂、盗号，发现后立马冻结平台,并调取你个人信息和提现账户信息,举报到网监局和当地派出所,你会有案底,同时还要追究你的责任,并赔偿账号全部损失.', '账号：\n密码：\n区服：\n角色名：\n联系方式：', 150.00, NULL, 7.50, 40.00, 30.00, 1, NULL, 30, NULL, '2025-07-24 05:07:13', '2025-07-24 05:07:13', 0);
INSERT INTO `tb_jmz_orders` VALUES (18, 'ORD1948248826800369664', 1948235993966456832, NULL, NULL, 1, 3, NULL, '指定李元芳【王者24星-王者30星】 铭文150超时不扣钱打完就验收', 1, '首先感谢大哥接我家订单，预祝您代练时顺利，注意身体，下面为我家的注意事项麻烦看下\n1:完成图没问题会秒结算（好友天梯榜首尾图）\n2:接单不打并怂恿号主走私单,一经发现,扣除双金,冻结账号,永久拉黑。\n3:接单第一时间联系号主,只要号主不找我们反馈问题,这边不会平白无故扣一分钱4:禁止开挂、盗号、辱骂号主，发现后立马冻结平台,并调取你个人信息和提现账户信息,举报到网监局和当地派出所,你会有案底,同时还要追究你的责任,并赔偿账号全部损失。 盗号、开挂的人别接我们订单了,感谢！', '账号：\n密码：\n区服：\n角色名：\n联系方式：', 30.00, NULL, 1.50, 10.00, 10.00, 1, NULL, 45, NULL, '2025-07-24 05:08:12', '2025-07-24 05:08:12', 0);
INSERT INTO `tb_jmz_orders` VALUES (19, 'ORD1948249064554491904', 1948235993966456832, NULL, NULL, 1, 3, NULL, '【星耀3 3星-王者1星】指定少司缘 铭文150 超时不扣钱打完就验收', 1, ':完成图没问题联系我们客服会秒结算（好友天梯榜首尾图）\n2:接单第一时间联系号主，只要号主不找我们反馈问题，这边不会平白无故扣一分钱\n3:如果接单不打并怂恿号主走私单,一经发现,扣除双金,冻结账号,永久拉黑\n4:禁止开挂、盗号、辱骂号主，发现后立马冻结平台,并调取你个人信息和提现账户信息,举报到网监局和当地派出所,你会有案底,同时还要追究你的责任,并赔偿账号全部损失\n订单问题可联系扣裙:壹零贰叁玖贰捌叁贰漆（102-392-8327）', '账号：\n密码：\n区服：\n角色名：\n联系方式：', 40.00, NULL, 2.00, 10.00, 10.00, 1, NULL, 30, NULL, '2025-07-24 05:09:08', '2025-07-24 05:09:08', 0);
INSERT INTO `tb_jmz_orders` VALUES (20, 'ORD1948249336731267072', 1948235993966456832, NULL, NULL, 1, 1, NULL, '指定后羿 【星耀3 1星-王者1星】 铭文150 超时不扣钱打完就验收', 1, '首先感谢大哥接我家订单，预祝您代练时顺利，注意身体，下面为我家的注意事项麻烦看下\n1:完成图没问题会秒结算（好友天梯榜首尾图）\n2:接单不打并怂恿号主走私单,一经发现,扣除双金,冻结账号,永久拉黑。\n3:接单第一时间联系号主,只要号主不找我们反馈问题,这边不会平白无故扣一分钱4:禁止开挂、盗号、辱骂号主，发现后立马冻结平台,并调取你个人信息和提现账户信息,举报到网监局和当地派出所,你会有案底,同时还', '账号：\n密码：\n区服：\n角色名：\n联系方式：', 17.00, NULL, 0.85, 10.00, 10.00, 1, NULL, 20, NULL, '2025-07-24 05:10:13', '2025-07-24 05:10:13', 0);
INSERT INTO `tb_jmz_orders` VALUES (21, 'ORD1948249706903760896', 1948235993966456832, NULL, NULL, 1, 4, NULL, '指定西施或小乔【星耀5 2星-王者1星】铭文150 超时不扣钱打完就验收', 1, '首先感谢大哥接我家订单，预祝您代练时顺利，注意身体，下面为我家的注意事项麻烦看下\n1:完成图没问题会秒结算，（完成图详情看订单要求）\n2:接单第一时间联系号主\n3:接单不打并怂恿号主走私单,一经发现,扣除双金,冻结账号,永久拉黑。\n4:禁止盗号、毁号,发现后立马冻结平台,并调取你个人信息和提现账户信息,举报到网监局和当地派出所,你会有案底,同时还要追究你的责任,并赔偿账号全部损失。 盗号、毁号的人别接我们订单了,感谢!', '账号：\n密码：\n区服：\n角色名：\n联系方式：', 200.00, NULL, 10.00, 10.00, 10.00, 1, NULL, 200, NULL, '2025-07-24 05:11:42', '2025-07-24 05:11:42', 0);

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
-- Records of tb_jmz_permission
-- ----------------------------
INSERT INTO `tb_jmz_permission` VALUES (27, '用户管理', 'user:manage', '用户管理');
INSERT INTO `tb_jmz_permission` VALUES (28, '角色管理', 'role:manage', '角色管理');
INSERT INTO `tb_jmz_permission` VALUES (29, '权限管理', 'permission:manage', '权限管理');

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
-- Records of tb_jmz_role
-- ----------------------------
INSERT INTO `tb_jmz_role` VALUES (1, '超级管理员', 'admin', '超级管理员');
INSERT INTO `tb_jmz_role` VALUES (2, '普通管理员', 'ordinaryAdmin', '普通管理员');
INSERT INTO `tb_jmz_role` VALUES (3, '客服', 'service', '客服');

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
-- Records of tb_jmz_role_menu
-- ----------------------------
INSERT INTO `tb_jmz_role_menu` VALUES (1, 1);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 1);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 2);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 2);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 3);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 3);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 4);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 4);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 5);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 5);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 6);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 6);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 7);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 7);
INSERT INTO `tb_jmz_role_menu` VALUES (3, 7);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 8);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 8);
INSERT INTO `tb_jmz_role_menu` VALUES (3, 8);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 10);
INSERT INTO `tb_jmz_role_menu` VALUES (3, 10);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 11);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 11);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 12);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 12);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 13);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 14);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 15);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 16);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 17);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 18);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 19);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 21);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 21);
INSERT INTO `tb_jmz_role_menu` VALUES (1, 22);
INSERT INTO `tb_jmz_role_menu` VALUES (2, 22);

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
-- Records of tb_jmz_role_permission
-- ----------------------------
INSERT INTO `tb_jmz_role_permission` VALUES (1, 27);
INSERT INTO `tb_jmz_role_permission` VALUES (2, 27);
INSERT INTO `tb_jmz_role_permission` VALUES (1, 28);
INSERT INTO `tb_jmz_role_permission` VALUES (2, 28);
INSERT INTO `tb_jmz_role_permission` VALUES (1, 29);

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
-- Records of tb_jmz_systems
-- ----------------------------
INSERT INTO `tb_jmz_systems` VALUES (1, '安卓QQ', 0, '2025-07-16/bd8584c9afad40f088a5f9f6bde83f73.png', '2025-07-16 10:04:40', '2025-07-16 10:04:40');
INSERT INTO `tb_jmz_systems` VALUES (2, '安卓微信', 1, '2025-07-16/03709409d4f749cb9ac8841b49d6570e.png', '2025-07-16 10:05:13', '2025-07-16 10:05:13');
INSERT INTO `tb_jmz_systems` VALUES (3, '苹果QQ', 2, '2025-07-16/8461b92030e34c1e9014ad7082f47aa3.png', '2025-07-16 10:09:03', '2025-07-16 10:09:03');
INSERT INTO `tb_jmz_systems` VALUES (4, '苹果微信', 3, '2025-07-16/36153d3eb19d44a1b3ca1ede5eec30c9.png', '2025-07-16 10:09:41', '2025-07-16 10:09:41');
INSERT INTO `tb_jmz_systems` VALUES (5, 'PC端', 4, '2025-07-16/22e59f3e4c314f05a298a56af359f284.png', '2025-07-16 10:10:40', '2025-07-16 10:10:40');
INSERT INTO `tb_jmz_systems` VALUES (6, '安卓网易手机', 5, '2025-07-23/b9e36f3bb25447e6b6c6e58e24f66cfa.png', '2025-07-23 14:46:40', '2025-07-23 14:46:40');
INSERT INTO `tb_jmz_systems` VALUES (7, '安卓网易邮箱', 6, '2025-07-23/a9f11b97ecf745bab5d250f2bb87a175.png', '2025-07-23 14:47:16', '2025-07-23 14:47:28');
INSERT INTO `tb_jmz_systems` VALUES (8, '苹果网易手机', 7, '2025-07-23/6b3da2b211034477b7c3753c753bda9d.png', '2025-07-23 14:48:01', '2025-07-23 14:48:11');
INSERT INTO `tb_jmz_systems` VALUES (9, '苹果网易邮箱', 8, '2025-07-23/691a6dbc50c24e9e8de1f046da766e75.png', '2025-07-23 14:48:34', '2025-07-23 14:48:41');
INSERT INTO `tb_jmz_systems` VALUES (10, '全端', 9, '2025-07-23/b2a742ef2981435ea65d9515029c2e1e.png', '2025-07-23 14:52:08', '2025-07-23 14:52:08');
INSERT INTO `tb_jmz_systems` VALUES (11, '亚服', 10, '2025-07-23/b6926cf6cebb43a5b78c1859febf6a1b.png', '2025-07-23 14:54:30', '2025-07-23 14:54:37');
INSERT INTO `tb_jmz_systems` VALUES (12, '管服', 11, '2025-07-23/b83a064d5b474aa8a9bff1b606f8800d.png', '2025-07-23 14:55:42', '2025-07-23 14:55:42');
INSERT INTO `tb_jmz_systems` VALUES (13, '渠道服', 11, '2025-07-23/7bdbf50c273e48be9abc2d8f0f0c75de.png', '2025-07-23 14:56:33', '2025-07-23 14:56:39');
INSERT INTO `tb_jmz_systems` VALUES (14, '官服', 13, '2025-07-23/7dd1db98a3824c9cb7808f13f248ac89.png', '2025-07-23 14:58:11', '2025-07-23 14:58:11');
INSERT INTO `tb_jmz_systems` VALUES (15, 'B服', 15, '2025-07-23/41e21784efca45329638a9eb9f9bb331.png', '2025-07-23 14:58:24', '2025-07-23 15:06:20');
INSERT INTO `tb_jmz_systems` VALUES (16, ' 电信区', 14, '2025-07-23/b968f3e01dab4cdba2355ff37c09d94d.png', '2025-07-23 15:00:07', '2025-07-23 15:00:07');
INSERT INTO `tb_jmz_systems` VALUES (17, '网通区', 15, '2025-07-23/cf414e1f803e4f7d89a3aede14e8374d.png', '2025-07-23 15:00:52', '2025-07-23 15:00:52');
INSERT INTO `tb_jmz_systems` VALUES (18, '全网通', 16, '2025-07-23/1ba51a99b5e34aa99d710f0b5f897294.png', '2025-07-23 15:01:12', '2025-07-23 15:01:12');
INSERT INTO `tb_jmz_systems` VALUES (19, '体验服', 17, '2025-07-23/ba92983434e04f83a443864e1701852d.png', '2025-07-23 15:01:38', '2025-07-23 15:01:38');

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
  `type` tinyint NOT NULL COMMENT '交易类型：1-充值，2-提现，3-订单收入，4-订单支出，5-退款，6-佣金，7-罚款',
  `amount` decimal(12, 2) NOT NULL COMMENT '交易金额',
  `balance_before` decimal(12, 2) NOT NULL COMMENT '交易前余额',
  `balance_after` decimal(12, 2) NOT NULL COMMENT '交易后余额',
  `status` tinyint NULL DEFAULT 1 COMMENT '交易状态：1-处理中，2-成功，3-失败',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '交易备注',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `transaction_no`(`transaction_no` ASC) USING BTREE,
  INDEX `idx_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_order`(`order_id` ASC) USING BTREE,
  INDEX `idx_transaction_no`(`transaction_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 170 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_jmz_transactions
-- ----------------------------
INSERT INTO `tb_jmz_transactions` VALUES (148, 1941702395802017793, 1941702395802017793, NULL, '1948018303486144512', 1, 10000.00, 0.00, 10000.00, 2, '管理员充值', '2025-07-23 13:52:11', '2025-07-23 13:52:11');
INSERT INTO `tb_jmz_transactions` VALUES (149, 1948235993966456832, 1941702395802017793, NULL, '1948239519031504896', 1, 1000.00, 0.00, 1000.00, 2, '', '2025-07-24 04:31:13', '2025-07-24 04:31:13');
INSERT INTO `tb_jmz_transactions` VALUES (150, 1948235994297806848, 1941702395802017793, NULL, '1948239581832818688', 1, 5000.00, 0.00, 5000.00, 2, '', '2025-07-24 04:31:28', '2025-07-24 04:31:28');
INSERT INTO `tb_jmz_transactions` VALUES (151, 1948235993966456832, 1941702395802017793, NULL, '1948239630058926080', 1, 0.01, 1000.00, 1000.01, 2, '4000', '2025-07-24 04:31:39', '2025-07-24 04:31:39');
INSERT INTO `tb_jmz_transactions` VALUES (152, 1948235993966456832, 1941702395802017793, NULL, '1948239657233821696', 7, 0.01, 1000.01, 1000.00, 2, '', '2025-07-24 04:31:45', '2025-07-24 04:31:45');
INSERT INTO `tb_jmz_transactions` VALUES (153, 1948235993966456832, 1941702395802017793, NULL, '1948239696807079936', 7, 0.01, 1000.00, 999.99, 2, '', '2025-07-24 04:31:55', '2025-07-24 04:31:55');
INSERT INTO `tb_jmz_transactions` VALUES (154, 1948235993966456832, 1941702395802017793, NULL, '1948244987225427968', 7, 0.02, 999.99, 999.97, 2, '', '2025-07-24 04:52:56', '2025-07-24 04:52:56');
INSERT INTO `tb_jmz_transactions` VALUES (155, 1948235993966456832, 1941702395802017793, NULL, '1948245042003038208', 6, 0.04, 999.97, 1000.01, 2, '', '2025-07-24 04:53:09', '2025-07-24 04:53:09');
INSERT INTO `tb_jmz_transactions` VALUES (156, 1948235993966456832, 1941702395802017793, NULL, '1948245073548398592', 9, 0.01, 1000.01, 1000.00, 2, '', '2025-07-24 04:53:17', '2025-07-24 04:53:17');
INSERT INTO `tb_jmz_transactions` VALUES (157, 1948235993966456832, 1941702395802017793, NULL, '1948245752325197824', 9, 100.00, 1000.00, 900.00, 2, '', '2025-07-24 04:55:59', '2025-07-24 04:55:59');
INSERT INTO `tb_jmz_transactions` VALUES (158, 1948063997005684736, 1941702395802017793, NULL, '1948247257719529472', 1, 5000.00, 0.00, 5000.00, 2, '', '2025-07-24 05:01:58', '2025-07-24 05:01:58');
INSERT INTO `tb_jmz_transactions` VALUES (159, 1948046413476671488, 1941702395802017793, NULL, '1948247299884867584', 1, 5000.00, 0.00, 5000.00, 2, '', '2025-07-24 05:02:08', '2025-07-24 05:02:08');
INSERT INTO `tb_jmz_transactions` VALUES (160, 1948063996443648000, 1941702395802017793, NULL, '1948247338690568192', 1, 5000.00, 0.00, 5000.00, 2, '', '2025-07-24 05:02:17', '2025-07-24 05:02:17');
INSERT INTO `tb_jmz_transactions` VALUES (161, 1948235993966456832, 1941702395802017793, NULL, '1948247540608557056', 1, 2000.00, 900.00, 2900.00, 2, '', '2025-07-24 05:03:05', '2025-07-24 05:03:05');
INSERT INTO `tb_jmz_transactions` VALUES (162, 1948235993966456832, 1948235993966456832, NULL, '1948247666404122624', 7, 100.00, 2900.00, 2800.00, 2, '订单提交冻结资金，订单号:ORD1948247665947045888', '2025-07-24 05:03:35', '2025-07-24 05:03:35');
INSERT INTO `tb_jmz_transactions` VALUES (163, 1948235993966456832, 1948235993966456832, NULL, '1948248011540815872', 7, 80.00, 2800.00, 2720.00, 2, '订单提交冻结资金，订单号:ORD1948248011444449280', '2025-07-24 05:04:57', '2025-07-24 05:04:57');
INSERT INTO `tb_jmz_transactions` VALUES (164, 1948235993966456832, 1948235993966456832, NULL, '1948248262142091264', 7, 200.00, 2720.00, 2520.00, 2, '订单提交冻结资金，订单号:ORD1948248262016364544', '2025-07-24 05:05:57', '2025-07-24 05:05:57');
INSERT INTO `tb_jmz_transactions` VALUES (165, 1948235993966456832, 1948235993966456832, NULL, '1948248578816237568', 7, 150.00, 2520.00, 2370.00, 2, '订单提交冻结资金，订单号:ORD1948248578736648192', '2025-07-24 05:07:13', '2025-07-24 05:07:13');
INSERT INTO `tb_jmz_transactions` VALUES (166, 1948235993966456832, 1948235993966456832, NULL, '1948248826892541952', 7, 30.00, 2370.00, 2340.00, 2, '订单提交冻结资金，订单号:ORD1948248826800369664', '2025-07-24 05:08:12', '2025-07-24 05:08:12');
INSERT INTO `tb_jmz_transactions` VALUES (167, 1948235993966456832, 1948235993966456832, NULL, '1948249064617304064', 7, 40.00, 2340.00, 2300.00, 2, '订单提交冻结资金，订单号:ORD1948249064554491904', '2025-07-24 05:09:08', '2025-07-24 05:09:08');
INSERT INTO `tb_jmz_transactions` VALUES (168, 1948235993966456832, 1948235993966456832, NULL, '1948249336789884928', 7, 17.00, 2300.00, 2283.00, 2, '订单提交冻结资金，订单号:ORD1948249336731267072', '2025-07-24 05:10:13', '2025-07-24 05:10:13');
INSERT INTO `tb_jmz_transactions` VALUES (169, 1948235993966456832, 1948235993966456832, NULL, '1948249706995933184', 7, 200.00, 2283.00, 2083.00, 2, '订单提交冻结资金，订单号:ORD1948249706903760896', '2025-07-24 05:11:42', '2025-07-24 05:11:42');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1948235994297806849 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_jmz_user
-- ----------------------------
INSERT INTO `tb_jmz_user` VALUES (1941702395802017793, 'admin', '18827850020', '$2a$10$iWn7QrfpmrqLEfGwPCJBQeHxWdMnJ1dcbdWVJsDCqH29/15Shg2/O', '2297794651@qq.com', '2025-07-21/6f53382439c84616bb03cd62a1123f70.png', '才冇有', 1, 1, '2025-07-06 11:42:11', '2025-07-23 16:08:01', 1, 0);
INSERT INTO `tb_jmz_user` VALUES (1948046413476671488, 'user2', '18827850019', '$2a$10$hhb45e24IkUTBuG6nTqbi.Xx6bm1z8zH4/3W2lZjhRcVH3yynNXi2', '2297794651@qq.cpm', NULL, NULL, 1, 1, NULL, '2025-07-24 04:08:59', 0, 0);
INSERT INTO `tb_jmz_user` VALUES (1948063996443648000, 'user3', '13800138000', '$2a$10$C8Rn5CaBpuxO3EJ4y0jcXOrSc7aNJjbMHWUxUE5UPpkF9XQJKWiZ.', 'user3@test.com', NULL, NULL, 1, 1, '2025-07-23 16:53:45', '2025-07-23 16:53:45', 0, 0);
INSERT INTO `tb_jmz_user` VALUES (1948063997005684736, 'user4', '13800138001', '$2a$10$wULxrKXuB1h14UEkXgTO1u0/5rRU5hNhm/GzPqA0hsNYmivI5ch2m', 'user3@test.com', NULL, NULL, 1, 1, '2025-07-23 16:53:45', '2025-07-23 16:53:45', 1, 0);
INSERT INTO `tb_jmz_user` VALUES (1948235993966456832, 'user5', '13800138010', '$2a$10$3dwxb0OHDJDKzk4T6/pNI.t0/Tx0CYyUDlafNforS8EHCHW5.882e', 'user5@test.com', NULL, NULL, 1, 1, '2025-07-24 04:17:12', '2025-07-24 04:17:12', 0, 0);
INSERT INTO `tb_jmz_user` VALUES (1948235994297806848, 'user6', '13800138011', '$2a$10$rTybJ4o0RfnfBtcJWG3FDeSj.y4VJvHHTPiVpbo3metqtmlQbUBi.', 'user6@test.com', NULL, NULL, 1, 1, '2025-07-24 04:17:12', '2025-07-24 04:17:12', 0, 0);

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
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_user`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_jmz_user_accounts
-- ----------------------------
INSERT INTO `tb_jmz_user_accounts` VALUES (16, 1941702395802017793, 10000.00, 0.00, 10000.00, 0.00, '2025-07-23 21:50:40', '2025-07-23 13:52:11');
INSERT INTO `tb_jmz_user_accounts` VALUES (17, 1948046413476671488, 5000.00, 0.00, 5000.00, 0.00, '2025-07-23 15:43:53', '2025-07-24 05:02:08');
INSERT INTO `tb_jmz_user_accounts` VALUES (18, 1948063996443648000, 5000.00, 0.00, 5000.00, 0.00, '2025-07-23 16:53:45', '2025-07-24 05:02:17');
INSERT INTO `tb_jmz_user_accounts` VALUES (19, 1948063997005684736, 5000.00, 0.00, 5000.00, 0.00, '2025-07-23 16:53:45', '2025-07-24 05:01:57');
INSERT INTO `tb_jmz_user_accounts` VALUES (20, 1948235993966456832, 2083.00, 817.00, 3000.01, 100.01, '2025-07-24 04:17:12', '2025-07-24 05:11:42');
INSERT INTO `tb_jmz_user_accounts` VALUES (21, 1948235994297806848, 5000.00, 0.00, 5000.00, 0.00, '2025-07-24 04:17:12', '2025-07-24 04:31:28');

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
-- Records of tb_jmz_user_game_boosting
-- ----------------------------
INSERT INTO `tb_jmz_user_game_boosting` VALUES (1948063997005684736, 1, '2025-07-23 18:00:37', '2025-07-23 18:00:37');
INSERT INTO `tb_jmz_user_game_boosting` VALUES (1948063997005684736, 3, '2025-07-23 18:00:37', '2025-07-23 18:00:37');
INSERT INTO `tb_jmz_user_game_boosting` VALUES (1948063997005684736, 4, '2025-07-23 18:00:37', '2025-07-23 18:00:37');
INSERT INTO `tb_jmz_user_game_boosting` VALUES (1948063997005684736, 5, '2025-07-23 18:00:37', '2025-07-23 18:00:37');
INSERT INTO `tb_jmz_user_game_boosting` VALUES (1948063997005684736, 8, '2025-07-23 18:00:37', '2025-07-23 18:00:37');
INSERT INTO `tb_jmz_user_game_boosting` VALUES (1948063997005684736, 9, '2025-07-23 18:00:37', '2025-07-23 18:00:37');
INSERT INTO `tb_jmz_user_game_boosting` VALUES (1948063997005684736, 15, '2025-07-23 18:00:37', '2025-07-23 18:00:37');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户实名认证表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_jmz_user_identity_verification
-- ----------------------------

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
-- Records of tb_jmz_user_role
-- ----------------------------
INSERT INTO `tb_jmz_user_role` VALUES (1941702395802017793, 1);
INSERT INTO `tb_jmz_user_role` VALUES (1948046413476671488, 2);
INSERT INTO `tb_jmz_user_role` VALUES (1948046413476671488, 3);
INSERT INTO `tb_jmz_user_role` VALUES (1948063996443648000, 3);
INSERT INTO `tb_jmz_user_role` VALUES (1948063997005684736, 3);

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

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
